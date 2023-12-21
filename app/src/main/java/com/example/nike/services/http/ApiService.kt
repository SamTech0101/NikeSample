package com.example.nike.services.http

import com.example.nike.data.*
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("product/list")
    fun getProducts(@Query("sort")sort:String):Single<List<Product>>
    @GET("banner/slider")
    fun getBanners(): Single<List<Banner>>
    @GET("comment/list")
    fun getComment(@Query("product_id") productId:Int):Single<List<Comment>>

    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject):Single<AddToCartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject): Single<MessageResponse>

    @GET("cart/list")
    fun getCart(): Single<CartResponse>

    @POST("cart/changeCount")
    fun changeCount(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemsCount(): Single<CartItemCount>

    @POST("auth/token")
    fun login(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("user/register")
    fun signUp(@Body jsonObject: JsonObject): Single<MessageResponse>

    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject): Call<TokenResponse>


}
/**
 * attention:
 * if (TokenContainer.token != null) is very important , because we have error 500 when token is null
 * */
fun createApiServiceInstance():ApiService{
    val okHttpClient=OkHttpClient.Builder().addInterceptor {
    val oldRequest=it.request()
        val newRequest=oldRequest.newBuilder()
        if (TokenContainer.token != null)
        newRequest.addHeader("Authorization","Bearer ${TokenContainer.token}")
        newRequest.addHeader("Accept", "application/json")
        newRequest.method(oldRequest.method(),oldRequest.body())
     return@addInterceptor it.proceed(newRequest.build())
    }.build()




    val retrofit=Retrofit.Builder()
        .baseUrl("http://test/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)

}