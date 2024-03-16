package com.cs4520.assignment4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cs4520.assignment4.database.entities.ProductEntity
import com.cs4520.assignment4.database.data.ProductDao

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var inst: ProductDatabase? = null
        fun getInstance(context: Context): ProductDatabase {
            return inst ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ProductDatabase::class.java, "product_database",).build()
                inst = instance
                instance
            }
        }
    }
}
