// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import sangria.schema.{ fields, Field, IntType, ObjectType, StringType }
import demo.model.Stratum
import demo.repo.MasterRepo

object StratumType {

  def apply[F[_]: Async]: ObjectType[MasterRepo[F], Stratum] =
    ObjectType(
      name = "Stratum",
      fieldsFn = () =>
        fields(
          Field(
            name        = "id",
            fieldType   = IntType,
            description = Some("Stratum id."),
            resolve     = _.value.id
          ),
          Field(
            name        = "name",
            fieldType   = StringType,
            description = Some("Stratum name."),
            resolve     = _.value.name
          )
        )
    )
}
