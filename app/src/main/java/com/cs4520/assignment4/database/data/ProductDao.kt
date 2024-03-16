package com.cs4520.assignment4.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cs4520.assignment4.database.entities.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE pageNumber = :pageNumber")
    suspend fun getProductsByPage(pageNumber: Int?): List<ProductEntity>

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<ProductEntity>

}
