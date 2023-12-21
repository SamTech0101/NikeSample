package com.example.nike.services

import com.example.nike.view.NikeImageView

interface ImageLoadingService {
    fun load(imageView: NikeImageView, imageUrl:String)
}