// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import sangria.schema.{ fields, Field, IntType, ObjectType, StringType }
import demo.model.ShopType
import demo.repo.MasterRepo

object ShopTypeType {

  def apply[F[_]: Async]: ObjectType[MasterRepo[F], ShopType] =
    ObjectType(
      name = "ShopType",
      fieldsFn = () =>
        fields(
          Field(
            name        = "id",
            fieldType   = IntType,
            description = Some("Shop type id."),
            resolve     = _.value.id
          ),
          Field(
            name        = "name",
            fieldType   = StringType,
            description = Some("Shop type name."),
            resolve     = _.value.name
          )
        )
    )
}
