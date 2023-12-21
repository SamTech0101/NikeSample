package com.example.nike.data.source

import com.example.nike.common.CLIENT_ID
import com.example.nike.common.CLIENT_SECRET
import com.example.nike.common.GRANT_TYPE
import com.example.nike.data.MessageResponse
import com.example.nike.data.TokenResponse
import com.example.nike.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single



class UserRemoteDataSource(private val apiService: ApiService) : UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        return apiService.login(JsonObject().apply {
            addProperty("username",username)
            addProperty("password",password)
            addProperty("grant_type",GRANT_TYPE)
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        })
    }

    override fun signUp(username: String, password: String): Single<MessageResponse> {
        return apiService.signUp(JsonObject().apply {
            addProperty("email",username)
            addProperty("password",password)
        })
    }
    // local dataSource

    override fun loadToken() {
        TODO("Not yet implemented")
    }
    // local dataSource

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

}