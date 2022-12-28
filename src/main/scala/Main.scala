// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo

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

object Main extends IOApp {
  //Add
  implicit val executionContext = unsafe.IORuntime.global.compute

  // Construct a transactor for connecting to the database.
  /*
  //No se porque razon no me permitia conectarme de esta forma
  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[F](32)
      ht <- HikariTransactor.newHikariTransactor[F](
              "org.postgresql.Driver",
              s"jdbc:postgresql:${System.getenv("DB_DATABASE")}",
              System.getenv("DB_USER"),
              System.getenv("DB_PASSWORD"),
              ec
            )
    } yield ht
  */
   def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[F](32)
      ht <- HikariTransactor.newHikariTransactor[F](
              "org.postgresql.Driver",
              "jdbc:postgresql:training",
              "user",
              "password",
              ec
            )
    } yield ht

  // Construct a GraphQL implementation based on our Sangria definitions.
  def graphQL[F[_]: Async](dispatcher: Dispatcher[F], transactor: Transactor[F]): WorldDeferredResolver[F] =
    WorldDeferredResolver[F](
      Schema(query = QueryType[F](dispatcher)),
      MasterRepo.fromTransactor(transactor).pure[F],
      executionContext
    )

  def graphRoutes[F[_]: Async](graph: WorldDeferredResolver[F]): HttpRoutes[F] = {
    object Dsl extends Http4sDsl[F]
    import Dsl._

    HttpRoutes.of[F] {
      case request @ GET -> Root / "graph" =>
        StaticFile fromResource ("/assets/playground.html", Some(request)) getOrElseF (NotFound())
      case request @ POST -> Root / "graph" =>
        request.as[Json].flatMap(graph.parse).flatMap {
          case Right(json) => Ok(json)
          case Left(json)  => BadRequest(json)
        }
    }
  }

  // Resource that mounts the given `routes` and starts a server.
  def server[F[_]: Async](
    routes: HttpRoutes[F]
  ): Resource[F, Server] =
    EmberServerBuilder
      .default[F]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(routes.orNotFound)
      .build

  // Resource that constructs our final server.
  def resource[F[_]: Async]: Resource[F, Server] =
    for {
      d  <- Dispatcher[F]
      xa <- transactor[F]
      gql = graphQL[F](d, xa)
      rts = graphRoutes[F](gql)
      svr <- server[F](rts)
    } yield svr

  // Our entry point starts the server and blocks forever.
  def run(args: List[String]): IO[ExitCode] =
    resource[IO].use(_ => IO.never.as(ExitCode.Success))
}
