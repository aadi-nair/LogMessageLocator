import scalaj.http.Http

import logfetcher.{LogRequest,LogResponse}

object GrpcClient {


  def run(logRequest: LogRequest): Int ={

    val request = Http("http://127.0.0.1:3000/fetchLogMessage")
      .headers(Map(
        "Content-Type" -> "application/grpc+proto",
        "Accept" -> "application/grpc+proto"
      ))
      .timeout(connTimeoutMs = 10000, readTimeoutMs = 10000)
      .postData(logRequest.toByteArray)

    val logResponse: LogResponse = LogResponse.parseFrom( request.asBytes.body)
    logResponse.count

  }

}
