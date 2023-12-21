package com.example.nike.data.repository

import com.example.nike.data.Banner
import com.example.nike.data.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(private val bannerRemoteDataSource: BannerDataSource):BannerRepository {
    override fun getBanners(): Single<List<Banner>> =bannerRemoteDataSource.getProduct()
    }
