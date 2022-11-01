import com.typesafe.config.{Config, ConfigFactory}
import io.grpc.ManagedChannel
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import logfetcher.*
import org.scalatest.matchers.should.Matchers



class gRPCTest extends AnyFlatSpec with Matchers:

  val applicationConf: Config = ConfigFactory.load("application.conf")
  it should "Contain uri to API Gateway" in {
    assert(applicationConf.hasPath("logMessageLocator.uri"))
  }

  it should "Contain port to rpc server" in {
    assert(applicationConf.hasPath("logMessageLocator.port"))
  }

  it should "Contain user-agent info" in {
    assert(applicationConf.hasPath("logMessageLocator.userAgent"))
  }

  it should "Contain API content-type" in {
    assert(applicationConf.hasPath("logMessageLocator.contentType"))
  }

  it should "Contain startTime argument" in {
    assert(applicationConf.hasPath("logMessageLocator.startTime"))
  }

  it should "Contain timeInterval argument" in {
    assert(applicationConf.hasPath("logMessageLocator.timeInterval"))
  }
  it should "Contain pattern argument" in {
    assert(applicationConf.hasPath("logMessageLocator.pattern"))
  }