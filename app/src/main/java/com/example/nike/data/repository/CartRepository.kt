package com.example.nike.data.repository

import com.example.nike.data.AddToCartResponse
import com.example.nike.data.CartItemCount
import com.example.nike.data.CartResponse
import com.example.nike.data.MessageResponse
import io.reactivex.Single

interface CartRepository {
    fun addToCart(productId: Int): Single<AddToCartResponse>
    fun get(): Single<CartResponse>
    fun remove(cartItemId: Int): Single<MessageResponse>
    fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse>
    fun getCartItemsCount(): Single<CartItemCount>
}