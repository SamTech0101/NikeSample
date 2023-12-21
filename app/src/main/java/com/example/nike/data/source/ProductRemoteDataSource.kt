package com.example.nike.data.source

import com.example.nike.data.Product
import com.example.nike.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(private val apiService: ApiService):ProductDataSource {
    override fun getProducts(sort:Int): Single<List<Product>> =apiService.getProducts(sort.toString())

    override fun addToFavorite(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFavorite(): Completable {
        TODO("Not yet implemented")
    }

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }
}