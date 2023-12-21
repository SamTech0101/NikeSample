package com.example.nike.data.repository

import com.example.nike.data.Comment
import io.reactivex.Single

interface CommentRepository {
    fun getAll(productId:Int):Single<List<Comment>>
    fun insert():Single<Comment>
}