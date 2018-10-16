package com.maogogo.cocoa.rest

import org.json4s.JsonAST.{ JNull, JString }
import org.json4s.{ CustomSerializer, DefaultFormats }

trait Json4sSupport extends de.heikoseeberger.akkahttpjson4s.Json4sSupport {

  implicit val serialization = org.json4s.native.Serialization
  implicit val formats = DefaultFormats + EmptyValueSerializer

}

private class EmptyValueSerializer
  extends CustomSerializer[String](
    _ ⇒
      ({
        case JNull ⇒ ""
        case JString(x) => x
      }, {
        case "" ⇒ JNull
      }))

private object EmptyValueSerializer extends EmptyValueSerializer
