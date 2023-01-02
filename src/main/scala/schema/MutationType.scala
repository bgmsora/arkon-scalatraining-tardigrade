// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import cats.effect.implicits._
import io.circe.generic.auto._
import sangria.macros.derive._
import sangria.marshalling.circe._
import sangria.schema._
import demo.model.{ Shop }
import demo.repo.MasterRepo
import cats.effect.std.Dispatcher

object MutationType {

  val CreateShopInput = Argument("input", ShopInput.apply)

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): ObjectType[MasterRepo[F], Unit] =
    ObjectType(
      name = "Mutation",
      fields = fields(
        Field(
          name = "createShop",
          OptionType(IntType),
          Some(
            """
								Creates a new shop.
								If already exist returns the id.
								If activity, stratum, shop_type entries doesn't exists returns -1.
							"""
          ),
          arguments = List(CreateShopInput),
          resolve = c => {
            def relationsExists(): Boolean =
              dispatcher.unsafeRunSync(c.ctx.activity.exists((c arg CreateShopInput).activityId)) &&
              dispatcher.unsafeRunSync(c.ctx.stratum.exists((c arg CreateShopInput).stratumId)) &&
              dispatcher.unsafeRunSync(c.ctx.shopType.exists((c arg CreateShopInput).shopTypeId))

            val exists = dispatcher.unsafeRunSync(c.ctx.shop.exists((c arg CreateShopInput).id))
            println("The store exists ",exists)
            println("Relation exists ",relationsExists())
            if (exists)
              Some((c arg CreateShopInput).id)
            else if (relationsExists()) 
              Some(dispatcher.unsafeRunSync(c.ctx.shop.create(c arg CreateShopInput)))
            else
              Some(-1)
          }
        )
      )
    )
}
