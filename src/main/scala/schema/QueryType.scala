// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import sangria.schema.{ fields, Argument, Field, ListType, ObjectType, OptionType, Schema, StringType, IDType}
import demo.repo.MasterRepo

object QueryType {
  val Id = Argument("id", StringType)

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): ObjectType[MasterRepo[F], Unit] =
    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name      = "activities",
          fieldType = ListType(ActivityType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.activity.fetchAll)
        ),
        Field(
          name      = "shopTypes",
          fieldType = ListType(ShopTypeType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.shopType.fetchAll)
        ),
        Field(
          name      = "stratums",
          fieldType = ListType(StratumType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.stratum.fetchAll)
        ),
        Field(
          name      = "stratumsById",
          fieldType = OptionType(StratumType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.stratum.fetchById(c arg Id)),
          arguments = List(Id)
        ),
        Field(
          name      = "shopTypesById",
          fieldType = OptionType(ShopTypeType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.shopType.fetchById(c arg Id)),
          arguments = List(Id)
        ),
        Field(
          name      = "activitiesById",
          fieldType = OptionType(ActivityType[F]),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.activity.fetchById(c arg Id)),
          arguments = List(Id)
        ),
        Field(
          name      = "shopById",
          fieldType = OptionType(ShopType[F](dispatcher)),
          resolve = c => dispatcher.unsafeToFuture(c.ctx.shop.fetchById(c arg Id)),
          arguments = List(Id)
        ),
        Field(
          name      = "shops",
          fieldType = ListType(ShopType[F](dispatcher)),
          resolve   = c => dispatcher.unsafeToFuture(c.ctx.shop.fetchAll)
        )
      )
    )
}
