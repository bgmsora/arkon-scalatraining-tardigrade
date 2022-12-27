// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import demo.model._
import io.chrisdavenport.log4cats.Logger
import cats.effect.Sync
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import demo.model.Stratum

trait StratumRepo[F[_]] {
  def fetchAll: F[List[Stratum]]
}

object StratumRepo {
  def fromTransactor[F[_]: Sync](xa: Transactor[F]): StratumRepo[F] =
    new StratumRepo[F] {
      val select: Fragment =
        fr"""
            SELECT id, name
            FROM stratum
          """

      def fetchAll: F[List[Stratum]] =
        select.query[Stratum].to[List].transact(xa)
    }
}