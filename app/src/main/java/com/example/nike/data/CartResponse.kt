package com.example.nike.data

data class CartResponse(
    val cart_items: List<CartItem>,
    val payable_price: Int,
    val shipping_cost: Int,
    val total_price: Int
)

data class PurchaseDetail(var totalPrice: Int, var shipping_cost: Int, var payable_price: Int)