import mill._
import mill.scalalib._
import coursier.maven.MavenRepository

// Import rocket-chip's build file components (backtick required for hyphen in dir name)
import $file.`rocket-chip`.common
import $file.`rocket-chip`.dependencies.hardfloat.common
import $file.`rocket-chip`.dependencies.cde.common
import $file.`rocket-chip`.dependencies.diplomacy.common
import $file.`rocket-chip`.dependencies.chisel.build

val scalaVer = "2.13.12"
val chiselVer = "6.7.0"

val chiselIvyDep       = ivy"org.chipsalliance::chisel:${chiselVer}"
val chiselPluginIvyDep = ivy"org.chipsalliance:::chisel-plugin:${chiselVer}"
val scalaReflect       = ivy"org.scala-lang:scala-reflect:${scalaVer}"
val sonatypeSnapshots = Seq(
  MavenRepository("https://s01.oss.sonatype.org/content/repositories/snapshots")
)

// -----------------------------------------------------------------------
// rocket-chip internal modules (mirrored from rocket-chip/build.sc)
// -----------------------------------------------------------------------

object macros extends millbuild.`rocket-chip`.common.MacrosModule with ScalaModule {
  def scalaVersion      = scalaVer
  def scalaReflectIvy   = scalaReflect
  override def millSourcePath = os.pwd / "rocket-chip" / "macros"
}

object cde extends millbuild.`rocket-chip`.dependencies.cde.common.CDEModule with ScalaModule {
  def scalaVersion = scalaVer
  override def millSourcePath = os.pwd / "rocket-chip" / "dependencies" / "cde" / "cde"
  def repositoriesTask = T.task(super.repositoriesTask() ++ sonatypeSnapshots)
}

object hardfloat extends millbuild.`rocket-chip`.dependencies.hardfloat.common.HardfloatModule with ScalaModule {
  def scalaVersion    = scalaVer
  def chiselModule    = None
  def chiselPluginJar = T(None)
  def chiselIvy       = Some(chiselIvyDep)
  def chiselPluginIvy = Some(chiselPluginIvyDep)
  override def millSourcePath = os.pwd / "rocket-chip" / "dependencies" / "hardfloat" / "hardfloat"
  def repositoriesTask = T.task(super.repositoriesTask() ++ sonatypeSnapshots)
}

object diplomacy extends millbuild.`rocket-chip`.dependencies.diplomacy.common.DiplomacyModule with ScalaModule {
  def scalaVersion    = scalaVer
  def chiselModule    = None
  def chiselPluginJar = T(None)
  def chiselIvy       = Some(chiselIvyDep)
  def chiselPluginIvy = Some(chiselPluginIvyDep)
  def cdeModule       = cde
  def sourcecodeIvy   = ivy"com.lihaoyi::sourcecode:0.3.1"
  override def millSourcePath = os.pwd / "rocket-chip" / "dependencies" / "diplomacy" / "diplomacy"
  def repositoriesTask = T.task(super.repositoriesTask() ++ sonatypeSnapshots)
}

object rocketchip extends millbuild.`rocket-chip`.common.RocketChipModule with ScalaModule {
  def scalaVersion    = scalaVer
  def chiselModule    = None
  def chiselPluginJar = T(None)
  def chiselIvy       = Some(chiselIvyDep)
  def chiselPluginIvy = Some(chiselPluginIvyDep)
  def macrosModule    = macros
  def hardfloatModule = hardfloat
  def cdeModule       = cde
  def diplomacyModule = diplomacy
  def diplomacyIvy    = None
  def mainargsIvy     = ivy"com.lihaoyi::mainargs:0.5.0"
  def json4sJacksonIvy = ivy"org.json4s::json4s-jackson:4.0.5"
  override def millSourcePath = os.pwd / "rocket-chip"
  def repositoriesTask = T.task(super.repositoriesTask() ++ sonatypeSnapshots)
}

// -----------------------------------------------------------------------
// Our SoC project
// -----------------------------------------------------------------------

object soc extends ScalaModule {
  def scalaVersion = scalaVer
  def moduleDeps   = Seq(rocketchip)

  def sources = T.sources(
    os.pwd / "src" / "main" / "scala"
  )

  def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
  )

  object test extends ScalaTests with TestModule.ScalaTest {
    def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.2.15")
  }
}
