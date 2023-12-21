package com.example.nike.data

data class Comment(
    val author: Author,
    val content: String,
    val date: String,
    val id: Int,
    val title: String
)

data class Author(
        val email: String
)