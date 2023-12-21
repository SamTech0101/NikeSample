package com.example.nike.data

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var cartItemProgressbarIsVisible:Boolean=false
)