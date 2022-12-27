name := "arkon-scalatraining"

version := "0.1"
scalaVersion := "2.13.8"       
libraryDependencies ++= Seq(    
  "io.circe" %% "circe-generic" % "0.14.2",    
  "io.circe" %% "circe-literal" % "0.14.2",    
  "io.circe" %% "circe-optics" % "0.14.1",    
  "org.http4s" %% "http4s-dsl" % "0.23.13",    
  "org.http4s" %% "http4s-ember-server" % "0.23.13",    
  "org.http4s" %% "http4s-ember-client" % "0.23.13",    
  "org.http4s" %% "http4s-circe" % "0.23.13",    
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",    
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC1",    
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",    
  "org.tpolecat" %% "doobie-postgres-circe" % "1.0.0-RC1",    
  "org.tpolecat" %% "doobie-specs2" % "1.0.0-RC1" % "test",    
  "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC1" % "test",    
  "org.typelevel" %% "cats-effect" % "3.3.12",    
  "net.postgis" % "postgis-jdbc" % "2.3.0",    
  "org.sangria-graphql" %% "sangria" % "2.0.0",    
  "org.sangria-graphql" %% "sangria-circe" % "1.3.0",   
   "org.slf4j" % "slf4j-nop" % "1.6.4"   
   )

/*
scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % "0.9.0",
  "org.tpolecat" %% "doobie-hikari" % "0.9.0",
  "org.tpolecat" %% "doobie-postgres" % "0.9.0",

  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.3",

  "co.fs2" %% "fs2-core" % "2.3.0",
  "co.fs2" %% "fs2-io" % "2.3.0",

  "org.sangria-graphql" %% "sangria" % "2.1.4",
  "org.sangria-graphql" %% "sangria-circe" % "1.3.0",

  "io.circe" %% "circe-core" % "0.13.0",
  "io.circe" %% "circe-generic" % "0.13.0",
  "io.circe" %% "circe-literal" % "0.13.0",
  "io.circe" %% "circe-optics" % "0.13.0",
  "io.circe" %% "circe-parser" % "0.13.0",

  "org.http4s" %% "http4s-dsl" % "0.21.4",
  "org.http4s" %% "http4s-blaze-client" % "0.21.4",
  "org.http4s" %% "http4s-blaze-server" % "0.21.4",
  "org.http4s" %% "http4s-circe" % "0.21.4",

  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.30"
)
*/

/*
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.3",

  "org.tpolecat"             %% "doobie-core" % "1.0.0-RC2",
  "org.tpolecat"             %% "doobie-hikari" % "1.0.0-RC2",
  "org.tpolecat"             %% "doobie-postgres" % "1.0.0-RC2",

  "org.sangria-graphql" %% "sangria-ast"           % "3.0.0",
  "org.sangria-graphql" %% "sangria-parser"        % "3.0.0",
  "org.sangria-graphql" %% "sangria-core"          % "3.0.0",
  "org.sangria-graphql" %% "sangria-derivation"    % "3.0.0",
  "org.sangria-graphql" %% "sangria-circe"         % "1.3.2",

   "io.circe" %% "circe-generic" % "0.14.1",
   "io.circe" %% "circe-parser"  % "0.14.1",

   "org.http4s" %% "http4s-blaze-server" % "0.23.11",
   "org.http4s" %% "http4s-circe"        % "0.23.11",
   "org.http4s" %% "http4s-dsl"          % "0.23.11",

  "org.scalatest" %% "scalatest"    % "3.2.9" % "test"
)
*/