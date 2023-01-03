package demo

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

object Retrieve {
  implicit val authResponseEntityDecoder: EntityDecoder[IO, ShopInegi]        = jsonOf
  implicit val authResponseEntityDecoder2: EntityDecoder[IO, List[ShopInegi]] = jsonOf

  def postToInegi(lat: String, lon: String): String = {
    val httpClient: Client[IO] = JavaNetClientBuilder[IO].create
    val a = Request[IO](
      method = Method.GET,
      uri = Uri.uri(
        "https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/21.85717833,-102.28487238/100/a4695ea4-7252-494f-b2e3-acf8ec61319f"
      )
    )
    val resp = httpClient.expect[List[ShopInegi]](a).map(datos => save(datos))

    return s"lat: $lat, lon: $lon, save = $resp"
  }

  def save(shops: List[ShopInegi]): String = {
    var total: String = " "
    var copy: String  = ""
    for (shop <- shops) {
      val a = shop.Nombre
      Console.println(a)
      total = copy.concat(a)
      copy  = total
    }
    var sent = s"Enviados: $total"
    return sent
  }

  def sentToGraphql(shop: ShopInegi): IO[String] = {
    val responseData=s"""
        {
          "query": "mutation CreateShop( shopInput: ShopInput!) { createShop(input: shopInput) }",
          "variables": {
            "shopInput": {
              "id": ${shop.Id.toInt},
              "shopTypeId": ${shop.Tipo.toInt},
              "stratumId": ${shop.Estrato.toInt},
              "activityId": ${shop.Clase_actividad.toInt},
              "address": ${shop.Calle + shop.Num_Exterior + shop.Colonia + shop.Ubicacion},
              "businessName": ${shop.Razon_social},
              "email": ${shop.Correo_e},
              "lat": ${shop.Latitud.toDouble},
              "long": ${shop.Longitud.toDouble},
              "name": ${shop.Nombre},
              "phoneNumber": ${shop.Telefono},
              "website": ${shop.Sitio_internet}
            }
          }
        }
      """.stripMargin
    val client: Client[IO] = JavaNetClientBuilder[IO].create
    val a = Request[IO](
      method = Method.POST,
      uri = Uri.uri(
        "http://localhost:8080/graphql"
      ),
      HttpVersion.`HTTP/2.0`,
      Headers(Header("Accept", "application/json")),
    )
    client.expect[String](a)
  }
}
