// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import sangria.schema.{ fields, Field, ListType, ObjectType, Schema }
import demo.repo.MasterRepo

object QueryType {

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
        )
      )
    )
}
