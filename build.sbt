scalaVersion := "2.13.18"

val chiselVersion = "7.11.0"

libraryDependencies ++= Seq(
  "org.chipsalliance" %% "chisel" % chiselVersion,
)

addCompilerPlugin(
  "org.chipsalliance" %% "chisel-plugin" % chiselVersion cross CrossVersion.full
)

// firtool 1.144.0 — MLIR/CIRCT backend used by Chisel 7.x
ThisBuild / firtoolVersion := "1.144.0"
