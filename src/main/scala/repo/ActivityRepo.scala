// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.effect.Sync
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.Activity

trait ActivityRepo[F[_]] {
  def fetchAll: F[List[Activity]]
}

object ActivityRepo {
  def fromTransactor[F[_]: Sync](xa: Transactor[F]): ActivityRepo[F] =
    new ActivityRepo[F] {
      val select: Fragment =
        fr"""
            SELECT id, name
            FROM comercial_activity
          """

      def fetchAll: F[List[Activity]] =
        select.query[Activity].to[List].transact(xa)
    }
}