package com.example.nike.data.source

import com.example.nike.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource{
    fun getProducts(sort:Int): Single<List<Product>>
    fun addToFavorite(): Completable
    fun deleteFavorite(): Completable
    fun getFavoriteProducts(): Single<List<Product>>
}