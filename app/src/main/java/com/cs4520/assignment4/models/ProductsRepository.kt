package com.cs4520.assignment4.models

import com.cs4520.assignment4.api.Api
import com.cs4520.assignment4.database.entities.ProductEntity
import com.cs4520.assignment4.database.data.ProductDao
import org.json.JSONObject
import java.net.UnknownHostException

class ProductsRepository(private val productDao: ProductDao) {
    class FetchException(message: String) : Exception(message)

    suspend fun fetch(pageNumber: Int): Set<Product> {
        val products =
            try {
                fetchProductsFromAPI(pageNumber)
            } catch (e: UnknownHostException) {
                fetchProductsFromDB(pageNumber)
            }

        return products
    }

    private suspend fun fetchProductsFromAPI(pageNumber: Int): Set<Product> {

        val response = Api.apiService.getProductsPage(pageNumber)

        if (response.isSuccessful) {
            val products = response.body() ?: emptySet()
            insertProductsIntoDB(products, pageNumber)
            return products

        } else {
            val errorObj = response.errorBody()?.string()
            val err =
                try {
                    val json = errorObj?.let { JSONObject(it) }
                    json?.getString("message")
                } catch (e: Exception) {
                    e.message
                }
            throw FetchException(err ?: " error")
        }
    }

    private suspend fun fetchProductsFromDB(pageNumber: Int): Set<Product> {
        val productEntities = productDao.getProductsByPage(pageNumber)

        if (productEntities.isEmpty()) {
            throw FetchException("No products available")
        }

        // map entities to products
        return productEntities.map { entity ->
            when (entity.type) {
                "Equipment" -> {
                    Product.Equipment(entity.name, entity.price)
                }
                "Food" -> {
                    Product.Food(entity.name, entity.expiryDate!!, entity.price)
                }
                else -> throw FetchException("Unknown product type: ${entity.type}")
            }
        }.toSet()
    }

    private suspend fun insertProductsIntoDB(
        products: Set<Product>,
        pageNumber: Int,
    ) {
        products.forEach { product ->
            val entity =
                when (product) {

                    is Product.Food -> {
                        ProductEntity(type = "Food", name = product.name, price = product.price,
                            expiryDate = product.expiryDate,
                            pageNumber = pageNumber,
                        )
                    }

                    is Product.Equipment -> {
                        ProductEntity(type = "Equipment", name = product.name, price = product.price,
                            pageNumber = pageNumber,
                        )
                    }

                }

            productDao.insertProduct(entity)
        }
    }
}
