// See README.md for license details.

ThisBuild / scalaVersion     := "2.13.7"
ThisBuild / version          := "0.1.1"
ThisBuild / organization     := "Cephasonics"

lazy val root = (project in file("."))
  .settings(
    name := "cs-beamer",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % "3.5.0",
      "edu.berkeley.cs" %% "chiseltest" % "0.5.0" % "test"
    ),
    scalacOptions ++= Seq(
      "-Xsource:2.13.7",
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit"//,
      // Enables autoclonetype2 in 3.4.x (on by default in 3.5)
      //"-P:chiselplugin:useBundlePlugin"
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.0" cross CrossVersion.full),
  )

