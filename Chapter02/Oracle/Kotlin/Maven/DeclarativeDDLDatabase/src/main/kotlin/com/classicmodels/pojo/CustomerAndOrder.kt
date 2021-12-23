package com.classicmodels.pojo

import java.time.LocalDate
import java.io.Serializable

data class CustomerAndOrder(val customerName: String?, 
                            val orderDate: LocalDate?): Serializable