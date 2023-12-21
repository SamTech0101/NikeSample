package com.example.nike.features.auth

import com.example.nike.common.CLIENT_ID
import com.example.nike.common.CLIENT_SECRET
import com.example.nike.common.GRANT_TYPE
import com.example.nike.data.TokenContainer
import com.example.nike.data.TokenResponse
import com.example.nike.data.source.UserDataSource
import com.example.nike.services.http.ApiService
import com.google.gson.JsonObject
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class NikeAuthenticator:okhttp3.Authenticator ,KoinComponent {
    val apiService:ApiService by inject()
    val userLocalDataSource: UserDataSource by inject()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.token!=null && TokenContainer.refreshToken!=null && !response.request().url().pathSegments().last().equals("token",false)) {

            try {
                //get new token
                val token = refreshToken()
                if (token.isEmpty())
                    return null
                //send new request
                return response.request().newBuilder().header("Authorization", "Bearer $token").build()

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        //if token & refreshToken are null -> return null
        return null
    }

    fun refreshToken():String{
        val response:retrofit2.Response<TokenResponse> =apiService.refreshToken(
                JsonObject().apply {
                    addProperty("grant_type", GRANT_TYPE)
                    addProperty("refresh_token", TokenContainer.refreshToken)
                    addProperty("client_id", CLIENT_ID)
                    addProperty("client_secret", CLIENT_SECRET)
                }
        ).execute()
        response.body()?.let {
            TokenContainer.update(it.access_token,it.refresh_token)
            userLocalDataSource.saveToken(it.access_token,it.refresh_token)
            return it.access_token
        }
        //below line : if (response.body() is null) -> return ""
                 return ""
    }

}
