import io.grpc.ManagedChannel
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.mockito.MockitoSugar
import logfetcher.*



class gRPCTest extends AnyFunSpec with MockitoSugar:
  describe("LogMessageCharCount.Map produces intermediate keys <timestamp, charCount>") {
    val blocking_stub = mock[LogFetcherGrpc.LogFetcherBlockingStub]
    val channel_stub = mock[ManagedChannel]
    val client = new GrpcClient(channel_stub, blocking_stub)

    client.fetchLogs("18:21:33.657", "00:01:00", "")

  }