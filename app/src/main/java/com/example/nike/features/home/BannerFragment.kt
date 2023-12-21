package com.example.nike.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_DATA
import com.example.nike.view.NikeImageView
import com.example.nike.data.Banner
import com.example.nike.services.ImageLoadingService
import org.koin.android.ext.android.inject
import java.lang.IllegalStateException

class BannerFragment:Fragment() {
    val imageLoadingService:ImageLoadingService by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val nikeImageView=inflater.inflate(R.layout.fragment_banner,container,false) as NikeImageView
        val banner=requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA)?:throw IllegalStateException("" +
                "banner can not null")
        imageLoadingService.load(nikeImageView ,banner.image)

        return nikeImageView
    }
    companion object{
        fun getInstance(banner: Banner):BannerFragment{
            val bannerFragment=BannerFragment()
            val bundle=Bundle()
            bundle.putParcelable(EXTRA_KEY_DATA,banner)
            bannerFragment.arguments=bundle
            return bannerFragment
        }
    }

}