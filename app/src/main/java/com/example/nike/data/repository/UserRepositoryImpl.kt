package com.example.nike.data.repository

import com.example.nike.data.TokenContainer
import com.example.nike.data.TokenResponse
import com.example.nike.data.source.UserDataSource

import io.reactivex.Completable

class UserRepositoryImpl(
    val userLocalDataSource: UserDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(it)
        }.ignoreElement()
    }

    override fun signUp(username: String, password: String): Completable {
        return userRemoteDataSource.signUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(it)
        }.ignoreElement()
    }


    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

   fun onSuccessfulLogin(tokenResponse: TokenResponse){
       TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
       userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
   }
}