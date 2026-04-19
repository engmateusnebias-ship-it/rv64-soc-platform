scalaVersion := "2.13.18"

val chiselVersion = "7.11.0"

libraryDependencies ++= Seq(
  "org.chipsalliance" %% "chisel" % chiselVersion,
)

addCompilerPlugin(
  "org.chipsalliance" %% "chisel-plugin" % chiselVersion cross CrossVersion.full
)
