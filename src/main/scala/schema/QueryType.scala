// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import sangria.schema.{Field, ListType, ObjectType, Schema, fields}
import demo.repo.MasterRepo

object QueryType {
  def apply[F[_]: Async]: ObjectType[MasterRepo[F], Unit] =
    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "activities",
          fieldType = ListType(ActivityType[F]),
          resolve = c => c.ctx.activity.fetchAll.toIO.unsafeToFuture
        ),
        Field(
          name = "shopTypes",
          fieldType = ListType(ShopTypeType[F]),
          resolve = c => c.ctx.shopType.fetchAll.toIO.unsafeToFuture
        ),
        Field(
          name = "stratums",
          fieldType = ListType(StratumType[F]),
          resolve = c => c.ctx.stratum.fetchAll.toIO.unsafeToFuture
        )
      )
    )

  def schema[F[_]: Async]: Schema[MasterRepo[F], Unit] =
    Schema(QueryType[F])
}