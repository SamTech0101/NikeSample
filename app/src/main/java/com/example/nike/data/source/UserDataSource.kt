package com.example.nike.data.source

import com.example.nike.data.MessageResponse
import com.example.nike.data.TokenResponse

import io.reactivex.Single

interface UserDataSource {

    fun login(username: String, password: String): Single<TokenResponse>
    fun signUp(username: String, password: String): Single<MessageResponse>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
}