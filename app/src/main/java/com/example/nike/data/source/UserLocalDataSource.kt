package com.example.nike.data.source

import android.content.SharedPreferences
import com.example.nike.data.*


import io.reactivex.Single

class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDataSource {
    //remote data Source
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }
    //remote data source

    override fun signUp(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
      TokenContainer.update(sharedPreferences.getString(ACCESS_TOKEN,null),sharedPreferences.getString(REFRESH_TOKEN,null))
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, token)
            putString(REFRESH_TOKEN, refreshToken)
        }.apply()
    }
}