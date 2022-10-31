import io.grpc.{Server, ServerBuilder}
import logfetcher.{LogFetcherGrpc, LogRequest, LogResponse}
import org.slf4j.Logger
import utils.CreateLogger
import scala.concurrent.{ExecutionContext, Future}
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.time.Duration
import scalapb.json4s.JsonFormat
import java.net.URI
import com.typesafe.config.{Config, ConfigFactory}


object LogGrpcServer {

  val applicationConf: Config = ConfigFactory.load("application.conf")
  val logger: Logger = CreateLogger(classOf[LogMessageLocator])
  def main(args: Array[String]): Unit = {
    val server: LogGrpcServer = new LogGrpcServer(ExecutionContext.global)

    server.start()
    server.blockUntilShutdown()
  }

  private val port = applicationConf.getInt("logMessageLocator.port")
}


class LogGrpcServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null
  val httpClient = HttpClient.newBuilder.version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build
  val applicationConf: Config = ConfigFactory.load("application.conf")


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
        .uri(URI.create(applicationConf.getString("logMessageLocator.uri")))
        .setHeader("User-Agent", applicationConf.getString("logMessageLocator.userAgent")) // add request header
        .header("Content-Type", applicationConf.getString("logMessageLocator.contentType"))
        .build();
      val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString)

      val proto: LogResponse = JsonFormat.fromJsonString[LogResponse](
        response.body)
      Future.successful(proto)
    }
  }

}