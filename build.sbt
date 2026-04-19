organization := "com.github.engmateusnebias-ship-it"
name         := "rv64-soc-platform"
version      := "0.1.0"

scalaVersion := "2.13.14"

val chiselVersion = "6.5.0"

libraryDependencies ++= Seq(
  "org.chipsalliance" %% "chisel" % chiselVersion,
)

addCompilerPlugin(
  "org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full
)

scalacOptions ++= Seq(
  "-language:reflectiveCalls",
  "-deprecation",
  "-feature",
  "-Xcheckinit",
)
