val scala3Version = "3.0.0"

enablePlugins(NativeImagePlugin)
Compile / mainClass := Some("scalad.run")

nativeImageOptions ++= List("-H:-CheckToolchain")

name := "scalad"
version := "0.1.0"

scalaVersion := scala3Version

libraryDependencies ++= Seq(
  "org.scala-lang" %% "scala3-tasty-inspector" % scala3Version,
  "org.benf" % "cfr" % "0.151",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)