// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.data._
import cats.effect._
import doobie._
import doobie.implicits._
import demo.model._
import cats.effect.Sync
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.ShopType

trait ShopTypeRepo[F[_]] {
  def fetchAll: F[List[ShopType]]
  def fetchById(id: String): F[Option[ShopType]]
  def exists(id: Int): F[Boolean]
}

object ShopTypeRepo {

  def fromTransactor[F[_]: Async](xa: Transactor[F]): ShopTypeRepo[F] =
    new ShopTypeRepo[F] {

      val select: Fragment =
        fr"""
            SELECT id, name
            FROM shop_type
          """

      def fetchAll: F[List[ShopType]] =
        select.query[ShopType].to[List].transact(xa)

      def fetchById(id: String): F[Option[ShopType]] =
        (select ++ fr"WHERE id = $id::INTEGER")
          .query[ShopType]
          .option
          .transact(xa)

      def exists(id: Int): F[Boolean] = {
        val selectExists = sql"""
  				select exists(
						select id from shop_type where id = $id
					)
				"""

        selectExists.query[Boolean].unique.transact(xa)
      }
    }
}
