package com.example.nike.features.auth

import com.example.nike.common.NikeView
import com.example.nike.data.repository.UserRepository
import io.reactivex.Completable

class AuthViewModel(val userRepository: UserRepository):NikeView.NikeViewModel() {

    fun login(username:String,password:String):Completable{
        _progressBarLiveData.value=true
        return userRepository.login(username, password).doFinally {
            _progressBarLiveData.postValue(false)
        }
    }
//The user credentials were incorrect////when signUp replace login
    fun signUp(username: String,password: String):Completable{
        _progressBarLiveData.value=true
        return userRepository.signUp(username, password).doFinally {
            _progressBarLiveData.postValue(false)
        }
    }


}