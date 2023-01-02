// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import cats.effect.implicits._
import sangria.schema._
import demo.model.Shop
import demo.repo.MasterRepo

object ShopType {

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): ObjectType[MasterRepo[F], Shop] =
    ObjectType(
      name = "Shop",
      fieldsFn = () =>
        fields(
          Field(
            name      = "id",
            fieldType = IntType,
            resolve   = _.value.id
          ),
          Field(
            name      = "address",
            fieldType = StringType,
            resolve   = _.value.address
          ),
          Field(
            name      = "businessName",
            fieldType = OptionType(StringType),
            resolve   = _.value.businessName
          ),
          Field(
            name      = "email",
            fieldType = OptionType(StringType),
            resolve   = _.value.email
          ),
          Field(
            name      = "lat",
            fieldType = FloatType,
            resolve   = _.value.lat
          ),
          Field(
            name      = "long",
            fieldType = FloatType,
            resolve   = _.value.long
          ),
          Field(
            name      = "name",
            fieldType = StringType,
            resolve   = _.value.name
          ),
          Field(
            name      = "phoneNumber",
            fieldType = OptionType(StringType),
            resolve   = _.value.phoneNumber
          ),
          Field(
            name      = "website",
            fieldType = OptionType(StringType),
            resolve   = _.value.website
          ),
          Field(
            name      = "activity",
            fieldType = OptionType(ActivityType[F]),
            resolve   = c => dispatcher.unsafeToFuture(c.ctx.activity.fetchById(c.value.activityId.toString))
          ),
          Field(
            name      = "shopType",
            fieldType = OptionType(ShopTypeType[F]),
            resolve   = c => dispatcher.unsafeToFuture(c.ctx.shopType.fetchById(c.value.shopTypeId.toString))
          ),
          Field(
            name      = "stratum",
            fieldType = OptionType(StratumType[F]),
            resolve   = c => dispatcher.unsafeToFuture(c.ctx.stratum.fetchById(c.value.stratumId.toString))
          )
        )
    )
}
