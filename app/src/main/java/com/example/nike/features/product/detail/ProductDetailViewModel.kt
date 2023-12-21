package com.example.nike.features.product.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.nike.common.EXTRA_KEY_DATA
import com.example.nike.common.NikeSingleObserver
import com.example.nike.common.NikeView
import com.example.nike.common.asyncNetworkRequest
import com.example.nike.data.Comment
import com.example.nike.data.Product
import com.example.nike.data.repository.CartRepository
import com.example.nike.data.repository.CommentRepository
import io.reactivex.Completable

class ProductDetailViewModel(bundle: Bundle,commentRepository: CommentRepository,val cartRepository: CartRepository): NikeView.NikeViewModel() {
    val productLiveData=MutableLiveData<Product>()
    val commentLiveData=MutableLiveData<List<Comment>>()
    init {
        _progressBarLiveData.value=true

        productLiveData.value=bundle.getParcelable(EXTRA_KEY_DATA)
        commentRepository.getAll(productLiveData.value!!.id)
                .asyncNetworkRequest()
                .doFinally { _progressBarLiveData.value=false }
                .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                    override fun onSuccess(t: List<Comment>) {
                        commentLiveData.value=t
                    }

                })
    }
    fun onAddToCartBtn(): Completable =cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()


}