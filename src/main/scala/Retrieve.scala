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
import _root_.io.circe.parser._

import org.http4s.client.dsl.io._
import org.http4s.ember.client._
import cats.effect.IO
import _root_.io.circe.generic.auto._

object Retrieve {
  implicit val authResponseEntityDecoder: EntityDecoder[IO, ShopInegi]        = jsonOf
  implicit val authResponseEntityDecoder2: EntityDecoder[IO, List[ShopInegi]] = jsonOf

  def postToInegi(lat: String, lon: String): IO[String] = {
    println("Entre al post to inegi")
    val httpClient: Client[IO] = JavaNetClientBuilder[IO].create
    val a = Request[IO](
      method = Method.GET,
      uri = Uri.uri(
        "https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/21.85717833,-102.28487238/100/a4695ea4-7252-494f-b2e3-acf8ec61319f"
      )
    )
    val resp = httpClient
      .expect[List[ShopInegi]](a)
      .flatMap(datos => saveShop(datos, httpClient))
      .map(resp => s"lat: $lat, lon: $lon, save = $resp")
    resp
  }

  def saveShop(shops: List[ShopInegi], client: Client[IO]): IO[String] = {
    println("Obtuve los datos para guardar")
    var total: String = " "
    var copy: String  = ""
    for (shop <- shops) {
      val a = shop.Nombre
      //sentToGraphql(shop, client).unsafeRunSync
      Console.println(a)
      total = copy.concat(a)
      copy  = total
    }
    var sent = s"Enviados: $total"
    println(sent)
    return IO.pure(sent)
  }

  def sentToGraphql(shop: ShopInegi, client: Client[IO]): IO[String] = {
    println("Sent to graphql")
    val rawJson = s"""{
          \"query\": \"{ mutation CreateShop( shopInput: ShopInput!) { createShop(input: shopInput) }\",
          \"variables\": {
            \"shopInput\": {
              \"id\": ${shop.Id.toInt},
              \"shopTypeId\": ${shop.Tipo.toInt},
              \"stratumId\": ${shop.Estrato.toInt},
              \"activityId\": ${shop.Clase_actividad.toInt},
              \"address\": \"${shop.Calle + shop.Num_Exterior + shop.Colonia + shop.Ubicacion}\",
              \"businessName\": \"${shop.Razon_social}\",
              \"email\": \"${shop.Correo_e}\",
              \"lat\": ${shop.Latitud.toDouble},
              \"long\": ${shop.Longitud.toDouble},
              \"name\": \"${shop.Nombre}\",
              \"phoneNumber\": \"${shop.Telefono}\",
              \"website\": \"${shop.Sitio_internet}\"
            }
          }
        }
      """.stripMargin
    println(rawJson)
    println("raw")
    //Json of circe
    val json: Json = parse(rawJson).getOrElse(Json.Null)
    //json to map
    val jsonAsMap = json.as[Map[String, String]]

    //Opcion 1
    /*
    //val client: Client[IO] = JavaNetClientBuilder[IO].create
    val a = Request[IO](
      method = Method.POST,
      uri = Uri.uri(
        "http://localhost:8080/graph"
      ),
      HttpVersion.`HTTP/2.0`,
      Headers(Header("Content-Type", "application/json"))
    )
    client.expect[String](a)
     */

    //Opcion 2
    //val client: Client[IO] = JavaNetClientBuilder[IO].create
    val req = POST(json, uri"http://localhost:8080/graph")
    client.expect[String](req)

  }
}
