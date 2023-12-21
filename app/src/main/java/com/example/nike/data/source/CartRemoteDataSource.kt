package com.example.nike.data.source

import com.example.nike.data.AddToCartResponse
import com.example.nike.data.CartItemCount
import com.example.nike.data.CartResponse
import com.example.nike.data.MessageResponse
import com.example.nike.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CartRemoteDataSource(private val apiService: ApiService):CartDataSource {
    override fun addToCart(productId: Int): Single<AddToCartResponse> =apiService.addToCart(
            JsonObject().apply {
                addProperty("product_id",productId)
            }
    )
    override fun get(): Single<CartResponse> = apiService.getCart()

    override fun remove(cartItemId: Int): Single<MessageResponse> = apiService.removeItemFromCart(
            JsonObject().apply {
                addProperty("cart_item_id", cartItemId)
            }
    )

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> =
            apiService.changeCount(JsonObject().apply {
                addProperty("cart_item_id", cartItemId)
                addProperty("count", count)
            })

    override fun getCartItemsCount(): Single<CartItemCount> = apiService.getCartItemsCount()
}