package com.example.nike.data.source

import com.example.nike.data.Banner
import io.reactivex.Single

interface BannerDataSource {
   fun getProduct():Single<List<Banner>>
}