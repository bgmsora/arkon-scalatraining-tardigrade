// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.effect.Async
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.Shop

trait ShopRepo[F[_]] {
  def fetchAll: F[List[Shop]]
  def fetchById(id: String): F[Option[Shop]]
}

object ShopRepo {

  def fromTransactor[F[_]: Async](xa: Transactor[F]): ShopRepo[F] =
    new ShopRepo[F] {

      val select: Fragment =
        fr"""
          SELECT id,
            address, business_name, email,

            ST_Y(position::GEOMETRY) AS lat,
            ST_X(position::GEOMETRY) AS long,

            name, phone_number, website,

            activity_id, shop_type_id, stratum_id
          FROM shop
          """

      def fetchAll: F[List[Shop]] =
        select.query[Shop].to[List].transact(xa)

      def fetchById(id: String): F[Option[Shop]] =
        (select ++ fr"WHERE id = $id::INTEGER").query[Shop].option.transact(xa)
    }
}
