// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package logfetcher

object LogfetcherProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq.empty
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      logfetcher.LogRequest,
      logfetcher.LogResponse
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """ChBsb2dmZXRjaGVyLnByb3RvIpkBCgpMb2dSZXF1ZXN0EiwKCXN0YXJ0VGltZRgBIAEoCUIO4j8LEglzdGFydFRpbWVSCXN0Y
  XJ0VGltZRI1Cgx0aW1lSW50ZXJ2YWwYAiABKAlCEeI/DhIMdGltZUludGVydmFsUgx0aW1lSW50ZXJ2YWwSJgoHcGF0dGVybhgDI
  AEoCUIM4j8JEgdwYXR0ZXJuUgdwYXR0ZXJuIi8KC0xvZ1Jlc3BvbnNlEiAKBWNvdW50GAEgASgFQgriPwcSBWNvdW50UgVjb3Vud
  DJBCgpMb2dGZXRjaGVyEjMKFEZldGNoTG9nc0ZvckludGVydmFsEgsuTG9nUmVxdWVzdBoMLkxvZ1Jlc3BvbnNlIgBiBnByb3RvM
  w=="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}