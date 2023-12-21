package com.example.nike.data

const val ACCESS_TOKEN="access_token"
const val REFRESH_TOKEN="refresh_token"
object TokenContainer {
    var token:String?=null
    private set
    var refreshToken:String?=null
    private set

    fun update(  token:String?,refreshToken:String?){
    this.token=token
        this.refreshToken=refreshToken
    }
}