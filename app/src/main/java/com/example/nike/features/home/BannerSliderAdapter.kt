package com.example.nike.features.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nike.data.Banner

class BannerSliderAdapter(fragment: Fragment, private val banners:List<Banner>):
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =banners.size

    override fun createFragment(position: Int): Fragment =BannerFragment.getInstance(banners[position])
}