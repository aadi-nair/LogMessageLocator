import com.typesafe.config.{Config, ConfigFactory}
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
//import scalaj.http.Http


import org.slf4j.Logger
import utils.CreateLogger
import logfetcher.*

import java.util.concurrent.TimeUnit

object GrpcClient {
  val applicationConf: Config = ConfigFactory.load("application.conf")

  def apply(host: String, port: Int): GrpcClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val blockingStub = LogFetcherGrpc.blockingStub(channel)
    new GrpcClient(channel, blockingStub)
  }
  

  def main(args: Array[String]): Unit = {

    val client = GrpcClient("localhost", applicationConf.getInt("logMessageLocator.port"))
    try {
      val startTime = applicationConf.getString("logMessageLocator.startTime")
      val timeInterval = applicationConf.getString("logMessageLocator.timeInterval")
      val pattern = applicationConf.getString("logMessageLocator.pattern")
      client.fetchLogs(startTime, timeInterval, pattern)
    } finally {
      client.shutdown()
    }

  }



}


class GrpcClient(  private val channel: ManagedChannel,
                   private val blockingStub: LogFetcherGrpc.LogFetcherBlockingStub){



  val logger: Logger = CreateLogger(classOf[LogMessageLocator])

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def fetchLogs(startTime: String, timeInterval: String, pattern: String): Unit = {
    logger.info("Fetching logs from" + startTime + " ...")
    val request = LogRequest(startTime = startTime, timeInterval = timeInterval, pattern =  pattern)
    try {
      val response = blockingStub.fetchLogsForInterval(request)
      logger.info("LogMessageCount: " + response.count)
    }
    catch {
      case e: StatusRuntimeException =>
        logger.warn("RPC failed: {0}", e.getStatus)
    }
  }



}