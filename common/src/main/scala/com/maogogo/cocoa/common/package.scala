package com.maogogo.cocoa

package object common {

  type ProtoBuf[T] = scalapb.GeneratedMessage with scalapb.Message[T]

}
