import sbt._
import Keys._
//import scalariform.formatter.preferences._

object Settings {

  lazy val basicSettings = Seq(
    organization := "com.maogogo",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    organizationName := "Maogogo Workshop",
    scalacOptions := Seq("-deprecation",
      "-feature",
      "-language:implicitConversions",
      "-language:postfixOps"),
    startYear := Some(2018),
    //    scalariformPreferences := scalariformPreferences.value
    //                              .setPreference(AlignSingleLineCaseStatements, true)
    //                              .setPreference(DoubleIndentConstructorArguments, true)
    //                              .setPreference(DanglingCloseParenthesis, Preserve),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
  )

}