// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.repo

import cats.effect._
import doobie._

final case class MasterRepo[F[_]](
  activity: ActivityRepo[F],
  shopType: ShopTypeRepo[F],
  stratum: StratumRepo[F]
)

object MasterRepo {

  def fromTransactor[F[_]: Async](xa: Transactor[F]): MasterRepo[F] =
    MasterRepo(
      ActivityRepo.fromTransactor(xa),
      ShopTypeRepo.fromTransactor(xa),
      StratumRepo.fromTransactor(xa)
    )
}
