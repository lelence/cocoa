name := "cocoa"

version := "0.1"

scalaVersion := "2.12.7"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies ++= {
  val akkaVersion = "2.5.17"

  Seq(
    "com.google.inject" % "guice" % "4.2.0",
    "net.codingwell" %% "scala-guice" % "4.2.1",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.1.5",

    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",

    "com.github.scopt" %% "scopt" % "3.7.0"
  )
}
//.map(_ withSources() withJavadoc())