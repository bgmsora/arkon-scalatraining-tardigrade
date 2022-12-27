// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo

/*
import cats.effect._
import cats.implicits._
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpRoutes, StaticFile, Uri}
//import sangria.schema.Schema
import demo.sangria.SangriaGraphQL
import demo.repo.MasterRepo
import demo.schema.{QueryType, WorldDeferredResolver}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global
 */

/*
import cats.effect._
import cats.implicits._
import demo.sangria.SangriaGraphQL
import demo.schema._
import doobie._
import doobie.hikari._
import doobie.util.ExecutionContexts
//import io.chrisdavenport.log4cats.Logger
//import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import repo._
import sangria._
import _root_.sangria.schema._
import org.http4s._
import org.http4s.dsl._
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze._
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global
 */

import demo.sangria.SangriaGraphQL
import demo.schema._
import repo._
import sangria._
import _root_.sangria.schema._

import cats.effect._
import cats.effect.std.Dispatcher
import cats.implicits._
import com.comcast.ip4s._
import doobie._
import doobie.hikari._
import io.circe._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.ember.server._
import org.http4s.server._
import sangria.schema.Schema

object Main extends IOApp {
  //Add
  implicit val executionContext = unsafe.IORuntime.global.compute
  // Construct a transactor for connecting to the database.
  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[F](32)
      ht <- HikariTransactor.newHikariTransactor[F](
              "org.postgresql.Driver",
              s"jdbc:postgresql:${System.getenv("DB_DATABASE")}",
              System.getenv("DB_USER"),
              System.getenv("DB_PASS"),
              ec
            )
    } yield ht

  // Construct a GraphQL implementation based on our Sangria definitions.
  def graphQL[F[_]: Async](
    transactor: Transactor[F]
  ): GraphQL[F] =
    SangriaGraphQL[F](
      Schema(
        query = QueryType[F]
      ),
      WorldDeferredResolver[F],
      MasterRepo.fromTransactor(transactor).pure[F]
    )

  // Playground or else redirect to playground
  def playgroundOrElse[F[_]: Async]: HttpRoutes[F] = {
    object dsl extends Http4sDsl[F]; import dsl._
    HttpRoutes.of[F] {
      case request @ GET -> Root / "playground.html" =>
        StaticFile
          .fromResource[F]("/assets/playground.html", Some(request))
          .getOrElseF(NotFound())

      case _ =>
        PermanentRedirect(Location(Uri.uri("/playground.html")))
    }
  }

  // Resource that mounts the given `routes` and starts a server.
  def server[F[_]: Async](
    routes: HttpRoutes[F]
  ): Resource[F, Server] =
    BlazeServerBuilder[F](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .resource

  // Resource that constructs our final server.
  def resource[F[_]: Async]: Resource[F, Server] =
    for {
      xa <- transactor[F]
      gql = graphQL[F](xa)
      rts = GraphQLRoutes[F](gql) <+> playgroundOrElse(b)
      svr <- server[F](rts)
    } yield svr

  // Our entry point starts the server and blocks forever.
  def run(args: List[String]): IO[ExitCode] =
    resource[IO].use(_ => IO.never.as(ExitCode.Success))
}
