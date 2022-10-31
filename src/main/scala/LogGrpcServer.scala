import io.grpc.{Server, ServerBuilder}
import logfetcher.{LogFetcherGrpc, LogRequest, LogResponse}
import org.slf4j.Logger
import utils.CreateLogger
import scala.concurrent.{ExecutionContext, Future}
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.time.Duration
import scalapb.json4s.JsonFormat
import java.net.URI


object LogGrpcServer {


  val logger: Logger = CreateLogger(classOf[LogMessageLocator])
  def main(args: Array[String]): Unit = {
    val server: LogGrpcServer = new LogGrpcServer(ExecutionContext.global)

    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}


class LogGrpcServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null
  val httpClient = HttpClient.newBuilder.version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build


  private def start(): Unit = {
    server = ServerBuilder.forPort(LogGrpcServer.port).addService(LogFetcherGrpc.bindService(new LogFetcherImpl, executionContext)).build.start
    LogGrpcServer.logger.info("Server started, listening on " + LogGrpcServer.port)
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class LogFetcherImpl extends LogFetcherGrpc.LogFetcher {

    override def fetchLogsForInterval(req: LogRequest) = {
      val req_json: String = JsonFormat.toJsonString(req)
      val request:HttpRequest = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(req_json))
        .uri(URI.create("http://127.0.0.1:3000/fetchLogMessage"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .header("Content-Type", "application/json")
        .build();
      val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString)

      val proto: LogResponse = JsonFormat.fromJsonString[LogResponse](
        response.body)
      Future.successful(proto)
    }
  }

}