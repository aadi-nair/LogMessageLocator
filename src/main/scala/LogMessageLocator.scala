//import GrpcClient
import logfetcher.{LogRequest,LogResponse}
import org.slf4j.{Logger, LoggerFactory}
import utils.CreateLogger

class LogMessageLocator {

  val logger: Logger = CreateLogger(classOf[LogMessageLocator])

  def run(): Unit ={

    val startTime = "18:49:27:897"
    val timeInterval = "00:01:00"
    val pattern = ""
    val req = LogRequest(startTime = startTime, timeInterval = timeInterval, pattern = pattern)
    val count = GrpcClient.run(req)
    logger.info(s"count: $count")
  }


}
