package com.example.nike.data.repository

import com.example.nike.data.Banner
import io.reactivex.Single

interface BannerRepository {
    fun getBanners():Single<List<Banner>>
}