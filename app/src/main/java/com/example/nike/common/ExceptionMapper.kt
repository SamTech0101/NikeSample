package com.example.nike.common

import com.example.nike.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class ExceptionMapper {
    companion object{
        fun map(throwable: Throwable):NikeException{
            if (throwable is HttpException){
                try {
                    val errorJsonObject=JSONObject(throwable.response()?.errorBody()!!.string())
                    val serverMessage=errorJsonObject.getString("message")
                    return NikeException(
                            if (throwable.code() == 401) {
                                NikeException.Type.AUTH
                            } else {
                                NikeException.Type.SIMPLE
                            }, serverMessage = serverMessage)

                }catch (e:Exception){Timber.e(e)}

            }

            return NikeException(NikeException.Type.SIMPLE, R.string.unknown_error)

        }
    }
}