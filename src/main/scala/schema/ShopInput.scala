package demo.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import sangria.schema._
import sangria.macros.derive._

import demo.repo.MasterRepo
import demo.model.Shop

object ShopInput {

  def apply: InputObjectType[Shop] =
    deriveInputObjectType[Shop](
      InputObjectTypeName("ShopInput"),
      InputObjectTypeDescription("A new shop")
    )
}
