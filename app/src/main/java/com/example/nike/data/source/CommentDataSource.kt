package com.example.nike.data.source

import com.example.nike.data.Comment
import io.reactivex.Single

interface CommentDataSource {

     fun getAll(productId:Int): Single<List<Comment>>
     fun insert(): Single<Comment>
}