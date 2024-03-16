package com.cs4520.assignment4.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment4.models.ProductsRepository
import com.cs4520.assignment4.models.Product
import kotlinx.coroutines.launch

class ProductsListsViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val fetchingData = MutableLiveData<Boolean>()
    private val productsData = MutableLiveData<Set<Product>>()

    val isFetching get() = fetchingData
    val products get() = productsData

    private val prevData = MutableLiveData(false)
    private val nextData = MutableLiveData(true)
    private val pageData = MutableLiveData(1)

    val hasPrev get() = prevData
    val hasNext get() = nextData
    val pageNumber get() = pageData

    private val errorMessageData = MutableLiveData<String>()
    val errorMessage get() = errorMessageData

    fun fetchProducts() {
        fetchingData.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.fetch(pageData.value ?: 1)

                // mssg for when api returns 0-zero results
                if (response.isEmpty()) {
                    throw ProductsRepository.FetchException("No products available")
                }

                productsData.postValue(response)
            } catch (e: ProductsRepository.FetchException) {
                errorMessageData.postValue(e.message)
            } finally {
                fetchingData.postValue(false)
            }
        }
    }

    fun nextPage() {
        val nextPage = (pageData.value ?: 1) + 1

        pageData.value = nextPage

        if (nextPage == 2)
            prevData.postValue(true)

        if (nextPage >= 4)
            nextData.postValue(false)

        fetchProducts()
    }

    fun prevPage() {

        val nextPage = (pageData.value ?: 1) - 1

        pageData.value = nextPage


        if (nextPage == 3)
            nextData.postValue(true)

        if (nextPage <= 1)
            prevData.postValue(false)

        fetchProducts()
    }

}
