name := "play-scala-shortener"

version := "0.0.1-SNAPSHOT"

lazy val root = (project in file("."))
     .enablePlugins(PlayScala)
     .settings (
         libraryDependencies += "org.scaldi" %% "scaldi-play" % "0.3.3",
         libraryDependencies += jdbc,
         libraryDependencies += anorm
     )

