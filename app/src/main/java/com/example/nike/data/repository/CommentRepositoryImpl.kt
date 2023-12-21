package com.example.nike.data.repository

import com.example.nike.data.Comment
import com.example.nike.data.source.CommentRemoteDataSource
import io.reactivex.Single

class CommentRepositoryImpl(private val commentRemoteDataSource: CommentRemoteDataSource):CommentRepository {
    override fun getAll(productId:Int): Single<List<Comment>> =commentRemoteDataSource.getAll(productId)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}