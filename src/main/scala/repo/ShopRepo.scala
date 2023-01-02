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
  def create(shop: Shop): F[Int]
  def exists(id: Int): F[Boolean]
  def filterByCoordinates(x: Double, y: Double, limit: Int): F[List[Shop]]
  def filterByRadius(x: Double, y: Double, radius: Int): F[List[Shop]]
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

      def exists(id: Int): F[Boolean] = {
        val selectExists = sql"""
  				select exists(
						select id from shop where id = $id
					)
				"""

        selectExists.query[Boolean].unique.transact(xa)
      }

      def filterByCoordinates(x: Double, y: Double, limit: Int): F[List[Shop]] =
        (select ++ fr"order by position <-> Geography(ST_MakePoint($x, $y))" ++ fr"limit $limit")
          .query[Shop]
          .to[List]
          .transact(xa)

      def filterByRadius(x: Double, y: Double, radius: Int): F[List[Shop]] =
        (select ++ fr"where ST_DWithin(Geography(position), Geography(ST_MakePoint($x, $y)), $radius)")
          .query[Shop]
          .to[List]
          .transact(xa)

      def create(shop: Shop): F[Int] = {
        val insert = sql"""
  				insert into shop
					values(
						${shop.id}, ${shop.name}, ${shop.businessName}, ${shop.activityId},
						${shop.stratumId}, ${shop.address}, ${shop.phoneNumber}, ${shop.email},
						${shop.website}, ${shop.shopTypeId}, ST_MakePoint(${shop.long}, ${shop.lat})::geography
					)
				"""
        insert.update.withUniqueGeneratedKeys[Int]("id").transact(xa)
      }
    }
}
