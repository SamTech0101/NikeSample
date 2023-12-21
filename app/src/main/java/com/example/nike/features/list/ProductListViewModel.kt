package com.example.nike.features.list

import androidx.lifecycle.MutableLiveData
import com.example.nike.R
import com.example.nike.common.NikeSingleObserver
import com.example.nike.common.NikeView
import com.example.nike.common.asyncNetworkRequest
import com.example.nike.data.Product
import com.example.nike.data.repository.ProductRepository

class ProductListViewModel( var sort:Int,private val productRepository: ProductRepository):NikeView.NikeViewModel() {
    val productLiveData=MutableLiveData<List<Product>>()
    val sortLiveData=MutableLiveData<Int>()
    private val sortTitles= arrayOf(R.string.sortLatest,R.string.sortPopular,R.string.sortPriceHighToLow,R.string.sortPriceLowToHigh)
    init {
        getProduct()

        sortLiveData.value=sortTitles[sort]
    }

 private  fun getProduct(){
        _progressBarLiveData.value=true
        productRepository.getProducts(sort)
            .asyncNetworkRequest()
            .doFinally { _progressBarLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    productLiveData.value=t
                }
            })
    }

    fun changeProductSortByIndex(sort: Int){
        this.sort=sort
        this.sortLiveData.value=sortTitles[sort]
        getProduct()
    }
}