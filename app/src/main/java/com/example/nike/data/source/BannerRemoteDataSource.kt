package com.example.nike.data.source

import com.example.nike.data.Banner
import com.example.nike.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService):BannerDataSource {
    override fun getProduct(): Single<List<Banner>> =apiService.getBanners()
}