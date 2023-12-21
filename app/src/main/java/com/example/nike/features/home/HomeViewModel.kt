package com.example.nike.features.home

import androidx.lifecycle.MutableLiveData
import com.example.nike.common.NikeSingleObserver
import com.example.nike.common.NikeView
import com.example.nike.common.asyncNetworkRequest
import com.example.nike.data.Banner
import com.example.nike.data.Product
import com.example.nike.data.SORT_LATEST
import com.example.nike.data.SORT_POPULAR
import com.example.nike.data.repository.BannerRepository
import com.example.nike.data.repository.ProductRepository

class HomeViewModel(productRepository: ProductRepository, bannerRepository: BannerRepository): NikeView.NikeViewModel() {
    val latestProductLiveData=MutableLiveData<List<Product>>()
    val popularProductLiveData=MutableLiveData<List<Product>>()
    val bannerLiveData=MutableLiveData<List<Banner>>()
    init {
      _progressBarLiveData.value=true

        productRepository.getProducts(SORT_LATEST)
                .asyncNetworkRequest()
                .doFinally { _progressBarLiveData.value=false }
                .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable){
                    override fun onSuccess(t: List<Product>) {
                        latestProductLiveData.value=t
                    }
                })
        productRepository.getProducts(SORT_POPULAR)
                .asyncNetworkRequest()
                .subscribe(object:NikeSingleObserver<List<Product>>(compositeDisposable){
                    override fun onSuccess(t: List<Product>) {
                        popularProductLiveData.value=t
                    }

                })
        bannerRepository.getBanners()
                .asyncNetworkRequest()
                .subscribe(object : NikeSingleObserver<List<Banner>>(compositeDisposable){
                    override fun onSuccess(t: List<Banner>) {
                        bannerLiveData.value=t
                    }

                })

    }

}