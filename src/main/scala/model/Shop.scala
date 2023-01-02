// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package demo.model

final case class Shop(
  id: Int,
  address: String,
  businessName: String,
  email: String,
  lat: Double,
  long: Double,
  name: String,
  phoneNumber: String,
  website: String,
  activityId: Int,
  shopTypeId: Int,
  stratumId: Int
)
