package com.maogogo.cocoa.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.maogogo.cocoa.common._
import scalapb.json4s.JsonFormat

package object socketio {

  implicit def string2ProtoBuf[T <: ProtoBuf[T]](
    implicit
    c: scalapb.GeneratedMessageCompanion[T]) = (json: String) ⇒ JsonFormat.fromJsonString[T](json)

  implicit def protoBuf2JavaMap[T <: ProtoBuf[T]] = (t: T) ⇒ {
    val mapper = new ObjectMapper()
    mapper.readValue(JsonFormat.toJsonString(t), classOf[java.util.Map[String, Any]])
  }

}
