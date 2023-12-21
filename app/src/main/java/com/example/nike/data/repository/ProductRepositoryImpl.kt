package com.example.nike.data.repository

import com.example.nike.data.Product
import com.example.nike.data.source.ProductDataSource
import com.example.nike.data.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(private val productRemoteDataSource: ProductDataSource,
                            private val productLocalDataSource: ProductLocalDataSource):ProductRepository {
    override fun getProducts(sort:Int): Single<List<Product>> =productRemoteDataSource.getProducts(sort)

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