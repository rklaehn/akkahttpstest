name := "akkahttpstest"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

mainClass in (Compile, run) := Some("httpclient.Main")

libraryDependencies += "com.typesafe.akka" %% "akka-stream-experimental" % "1.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "1.0"
