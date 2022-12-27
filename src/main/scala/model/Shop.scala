// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.model

final case class Shop(
  id: Int,
  activity: Activity,
  address: String,
  email: String,
  businessName: String,
  lat: Float,
  long: Float,
  name: String,
  phoneNumber: String,
  shopType: ShopType,
  stratum: Stratum,
  website: String
)
