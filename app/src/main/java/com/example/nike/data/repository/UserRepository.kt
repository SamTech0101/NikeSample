package com.example.nike.data.repository

import io.reactivex.Completable

interface UserRepository {

    fun login(username: String, password: String): Completable
    fun signUp(username: String, password: String): Completable
    fun loadToken()
}