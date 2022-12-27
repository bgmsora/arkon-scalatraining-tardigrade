// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.schema

import cats.effect.Async
import sangria.schema.{ fields, Field, IntType, ObjectType, StringType }
import demo.model.Activity
import demo.repo.MasterRepo

object ActivityType {

  def apply[F[_]: Async]: ObjectType[MasterRepo[F], Activity] =
    ObjectType(
      name = "Activity",
      fieldsFn = () =>
        fields(
          Field(
            name        = "id",
            fieldType   = IntType,
            description = Some("Activity id."),
            resolve     = _.value.id
          ),
          Field(
            name        = "name",
            fieldType   = StringType,
            description = Some("Activity name."),
            resolve     = _.value.name
          )
        )
    )
}
