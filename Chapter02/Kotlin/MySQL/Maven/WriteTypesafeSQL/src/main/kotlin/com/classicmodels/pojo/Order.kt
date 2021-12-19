package com.classicmodels.pojo

import java.time.LocalDate
import java.io.Serializable

data class Order(val orderId: Long?, 
                 val orderDate: LocalDate?,
                 val requiredDate: LocalDate?,
                 val shippedDate: LocalDate?,
                 val comments: String?,
                 val customerNumber: Long?): Serializable