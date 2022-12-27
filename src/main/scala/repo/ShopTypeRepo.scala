// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.data._
import cats.effect._
import doobie._
import doobie.implicits._
import demo.model._
import io.chrisdavenport.log4cats.Logger
import cats.effect.Sync
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.ShopType

trait ShopTypeRepo[F[_]] {
  def fetchAll: F[List[ShopType]]
}

object ShopTypeRepo {
  def fromTransactor[F[_]: Sync](xa: Transactor[F]): ShopTypeRepo[F] =
    new ShopTypeRepo[F] {
      val select: Fragment =
        fr"""
            SELECT id, name
            FROM shop_type
          """

      def fetchAll: F[List[ShopType]] =
        select.query[ShopType].to[List].transact(xa)
    }
}