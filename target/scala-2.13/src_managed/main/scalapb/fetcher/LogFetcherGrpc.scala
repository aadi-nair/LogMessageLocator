// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package fetcher


object LogFetcherGrpc {
  val METHOD_FETCH_LOGS_FOR_INTERVAL: _root_.io.grpc.MethodDescriptor[fetcher.LogRequest, fetcher.LogResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("LogFetcher", "FetchLogsForInterval"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[fetcher.LogRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[fetcher.LogResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(fetcher.FetcherProto.javaDescriptor.getServices().get(0).getMethods().get(0)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("LogFetcher")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(fetcher.FetcherProto.javaDescriptor))
      .addMethod(METHOD_FETCH_LOGS_FOR_INTERVAL)
      .build()
  
  trait LogFetcher extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = LogFetcher
    def fetchLogsForInterval(request: fetcher.LogRequest): scala.concurrent.Future[fetcher.LogResponse]
  }
  
  object LogFetcher extends _root_.scalapb.grpc.ServiceCompanion[LogFetcher] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[LogFetcher] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = fetcher.FetcherProto.javaDescriptor.getServices().get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = fetcher.FetcherProto.scalaDescriptor.services(0)
    def bindService(serviceImpl: LogFetcher, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_FETCH_LOGS_FOR_INTERVAL,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[fetcher.LogRequest, fetcher.LogResponse] {
          override def invoke(request: fetcher.LogRequest, observer: _root_.io.grpc.stub.StreamObserver[fetcher.LogResponse]): _root_.scala.Unit =
            serviceImpl.fetchLogsForInterval(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  trait LogFetcherBlockingClient {
    def serviceCompanion = LogFetcher
    def fetchLogsForInterval(request: fetcher.LogRequest): fetcher.LogResponse
  }
  
  class LogFetcherBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LogFetcherBlockingStub](channel, options) with LogFetcherBlockingClient {
    override def fetchLogsForInterval(request: fetcher.LogRequest): fetcher.LogResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_FETCH_LOGS_FOR_INTERVAL, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LogFetcherBlockingStub = new LogFetcherBlockingStub(channel, options)
  }
  
  class LogFetcherStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LogFetcherStub](channel, options) with LogFetcher {
    override def fetchLogsForInterval(request: fetcher.LogRequest): scala.concurrent.Future[fetcher.LogResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_FETCH_LOGS_FOR_INTERVAL, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LogFetcherStub = new LogFetcherStub(channel, options)
  }
  
  object LogFetcherStub extends _root_.io.grpc.stub.AbstractStub.StubFactory[LogFetcherStub] {
    override def newStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LogFetcherStub = new LogFetcherStub(channel, options)
    
    implicit val stubFactory: _root_.io.grpc.stub.AbstractStub.StubFactory[LogFetcherStub] = this
  }
  
  def bindService(serviceImpl: LogFetcher, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = LogFetcher.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): LogFetcherBlockingStub = new LogFetcherBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): LogFetcherStub = new LogFetcherStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = fetcher.FetcherProto.javaDescriptor.getServices().get(0)
  
}