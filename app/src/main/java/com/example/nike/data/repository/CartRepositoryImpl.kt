package com.example.nike.data.repository

import com.example.nike.data.AddToCartResponse
import com.example.nike.data.CartItemCount
import com.example.nike.data.CartResponse
import com.example.nike.data.MessageResponse
import com.example.nike.data.source.CartRemoteDataSource
import io.reactivex.Single

class CartRepositoryImpl(val cartRemoteDataSource: CartRemoteDataSource) :CartRepository{
    override fun addToCart(productId: Int): Single<AddToCartResponse> =cartRemoteDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> =cartRemoteDataSource.get()

    override fun remove(cartItemId: Int): Single<MessageResponse> =cartRemoteDataSource.remove(cartItemId)

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> =cartRemoteDataSource.changeCount(cartItemId, count)
    override fun getCartItemsCount(): Single<CartItemCount> =cartRemoteDataSource.getCartItemsCount()
}

