package com.example.nike.data.source

import com.example.nike.data.Comment
import com.example.nike.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService) :CommentDataSource {
    override fun getAll(productId:Int): Single<List<Comment>> =apiService.getComment(productId)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}