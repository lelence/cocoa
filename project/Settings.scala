import sbt._
import Keys._

object Settings {

	lazy val basicSettings = Seq(
		organization := "com.maogogo",
	  version := "0.0.1-SNAPSHOT",
	  scalaVersion := "2.12.7"
	)

}