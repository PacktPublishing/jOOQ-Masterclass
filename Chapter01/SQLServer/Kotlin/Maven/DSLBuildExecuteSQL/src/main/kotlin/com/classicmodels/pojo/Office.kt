package com.classicmodels.pojo

import java.io.Serializable

data class Office(val officecode: String?, 
                  val addressLineFirst: String?,
                  val addressLineSecond: String?,
                  val city: String?,
                  val country: String?,
                  val phone: String?,
                  val postalCode: String?,
                  val state: String?,
                  val location: Any?,
                  val territory: String?,
                  val internalBudget: Int?): Serializable