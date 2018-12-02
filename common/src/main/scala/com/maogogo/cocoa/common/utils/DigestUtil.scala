package com.maogogo.cocoa.common.utils

import java.security.MessageDigest
import java.math.BigInteger

object DigestUtil extends App {

  private lazy val md5 = (bs: Array[Byte]) ⇒
    MessageDigest.getInstance("MD5").digest("0".getBytes())


}
