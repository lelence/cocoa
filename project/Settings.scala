import sbt._
import Keys._

object Settings {

  lazy val basicSettings = Seq(
    organization := "com.maogogo",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    organizationName := "Maogogo Workshop",
    startYear := Some(2018),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
  )

}