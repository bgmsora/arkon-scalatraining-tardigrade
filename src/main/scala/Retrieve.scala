import demo.model.ShopInegi
import org.http4s.client.Client

import io.circe.generic.auto._
import org.http4s.circe._
import cats.effect._
import org.http4s.EntityDecoder

import org.http4s.{Method,UrlForm}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.Request
import org.http4s.Uri
import org.http4s.client.dsl._
import org.http4s.dsl.impl._
import org.http4s.dsl._

object Retrieve {
  implicit val authResponseEntityDecoder: EntityDecoder[IO, ShopInegi] = jsonOf
  implicit val authResponseEntityDecoder2: EntityDecoder[IO, List[ShopInegi]] = jsonOf
  
  def postTo(client: Client[IO]) = {
    val a = Request[IO](method = Method.GET,uri = Uri.uri("https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/21.85717833,-102.28487238/100/a4695ea4-7252-494f-b2e3-acf8ec61319f"))

    client.expect[List[ShopInegi]](a)
  }
}
