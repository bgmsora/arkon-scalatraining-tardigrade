package demo

import cats.syntax.all._

import demo.model.ShopInegi
import org.http4s.client.Client

import io.circe.generic.auto._
import org.http4s.circe._
import cats.effect._
import org.http4s.EntityDecoder

import org.http4s.{ Method, UrlForm }
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.Request
import org.http4s.Uri
import org.http4s.client.dsl._
import org.http4s.dsl.impl._
import org.http4s.dsl._

import org.http4s._, org.http4s.dsl.io._
import org.http4s.client.JavaNetClientBuilder

import cats.effect.unsafe.implicits.global
import org.http4s.implicits._
import _root_.io.circe._
import _root_.io.circe.parser._

import org.http4s.client.dsl.io._
import org.http4s.ember.client._
import _root_.io.circe.generic.auto._

import java.nio.charset

class Retrieve[F[_]: Async] {
  implicit val authResponseEntityDecoder: EntityDecoder[F, ShopInegi]        = jsonOf
  implicit val authResponseEntityDecoder2: EntityDecoder[F, List[ShopInegi]] = jsonOf

  def postToInegi(lat: String, lon: String): F[String] = {
    val httpClient: Client[F] = JavaNetClientBuilder[F].create
    val a = Request[F](
      method = Method.GET,
      uri = Uri.uri(
        "https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/21.85717833,-102.28487238/100/a4695ea4-7252-494f-b2e3-acf8ec61319f"
      )
    )
    for {
      shops  <- httpClient.expect[List[ShopInegi]](a)
      _      <- Async[F].delay(println(s"$shops Test postToInegi"))
      result <- saveShop(shops, httpClient)
    } yield s"lat: $lat, lon: $lon, save = $result"
  }

  def saveShop(shops: List[ShopInegi], client: Client[F]): F[String] =
    for {
      _ <- Async[F].delay(println("Obtuve los datos para guardar"))
      aux <- shops
               .traverse(sentToGraphql(_, client))
               .handleErrorWith(e => Async[F].delay(println(s"$e Error de sentToGraphql")) *> Async[F].raiseError(e))
      result = aux.mkString
    } yield result

  def sentToGraphql(shop: ShopInegi, client: Client[F]): F[String] = {
    println(shop)
    println(shop.Estrato)
    println(shop.Tipo)
    println(shop.Clase_actividad)
    val rawJson = s"""{
          "query": "mutation CreateShop( $$shopInput: ShopInput!) { createShop(input: $$shopInput) }",
					"variables": {
            "shopInput": {
              "id": ${shop.Id.toIntOption.getOrElse(1)},
              "shopTypeId": ${shop.Tipo.toIntOption.getOrElse(1)},
              "stratumId": ${shop.Estrato.toIntOption.getOrElse(1)},
              "activityId": ${shop.Clase_actividad.toIntOption.getOrElse(1)},
              "address": "${shop.Calle + shop.Num_Exterior + shop.Colonia + shop.Ubicacion}",
              "businessName": "${shop.Razon_social}",
              "email": "${shop.Correo_e}",
              "lat": ${shop.Latitud.toDoubleOption.getOrElse(0.0)},
              "long": ${shop.Longitud.toDoubleOption.getOrElse(0.0)},
              "name": "${shop.Nombre}",
              "phoneNumber": "${shop.Telefono}",
              "website": "${shop.Sitio_internet}"
					  }
           }
					}""".stripMargin
    println(rawJson)
    println("raw")

    val a = Request[F](
      method = Method.POST,
      uri = Uri.uri(
        "http://localhost:8080/graph"
      ),
      HttpVersion.`HTTP/2.0`,
      Headers(Header("Content-Type", "application/json"))
    ).withBodyStream(fs2.Stream.emit(rawJson).through(fs2.text.encode(charset.Charset.forName("UTF-8"))))

    for {
      _ <- Async[F].delay(println("Sent to graphql"))
      result <-
        client
          .expect[String](a)
          .handleErrorWith(e => Async[F].delay(println(s"${e.getMessage()} Error al enviar post a graphql")) *> Async[F].raiseError(e))
    } yield result
  }
}
