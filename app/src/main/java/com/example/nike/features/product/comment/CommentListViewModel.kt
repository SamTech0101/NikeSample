package com.example.nike.features.product.comment

import androidx.lifecycle.MutableLiveData
import com.example.nike.common.NikeSingleObserver
import com.example.nike.common.NikeView
import com.example.nike.common.asyncNetworkRequest
import com.example.nike.data.Comment
import com.example.nike.data.repository.CommentRepository

class CommentListViewModel(productId:Int,commentRepository: CommentRepository):NikeView.NikeViewModel() {
    val commentLiveData=MutableLiveData<List<Comment>>()
    init {
        _progressBarLiveData.value=true
        commentRepository.getAll(productId)
                .asyncNetworkRequest()
                .doFinally { _progressBarLiveData.value=false }
                .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                    override fun onSuccess(t: List<Comment>) {
                        commentLiveData.value=t
                    }

                })
    }
}