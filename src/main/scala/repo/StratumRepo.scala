// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import demo.model._
import cats.effect.Async
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.Stratum

trait StratumRepo[F[_]] {
  def fetchAll: F[List[Stratum]]
  def fetchById(id: String): F[Option[Stratum]]
  def exists(id: Int): F[Boolean]
}

object StratumRepo {

  def fromTransactor[F[_]: Async](xa: Transactor[F]): StratumRepo[F] =
    new StratumRepo[F] {

      val select: Fragment =
        fr"""
            SELECT id, name
            FROM stratum
          """

      def fetchAll: F[List[Stratum]] =
        select.query[Stratum].to[List].transact(xa)

      def fetchById(id: String): F[Option[Stratum]] =
        (select ++ fr"WHERE id = $id::INTEGER")
          .query[Stratum]
          .option
          .transact(xa)

      def exists(id: Int): F[Boolean] = {
        val selectExists = sql"""
  				select exists(
						select id from stratum where id = $id
					)
				"""

        selectExists.query[Boolean].unique.transact(xa)
      }
    }
}
