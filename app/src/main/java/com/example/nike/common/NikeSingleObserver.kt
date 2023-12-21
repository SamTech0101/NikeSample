package com.example.nike.common

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

abstract class NikeSingleObserver<T>(private val compositeDisposable: CompositeDisposable) :SingleObserver<T> {
    override fun onError(e: Throwable) {
        EventBus.getDefault().post(ExceptionMapper.map(e))
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }
}