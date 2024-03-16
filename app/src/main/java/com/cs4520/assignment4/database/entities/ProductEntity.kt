package com.cs4520.assignment4.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(val type: String,
    @PrimaryKey
    val name: String, val price: Float,
                         val expiryDate: String? = null, val pageNumber: Int? = null, )
