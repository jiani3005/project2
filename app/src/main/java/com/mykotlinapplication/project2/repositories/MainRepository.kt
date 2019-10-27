package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mykotlinapplication.project2.models.ApiInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainRepository {

    private val TAG = "MainRepository"
    private var apiResponse = MutableLiveData<String>()

//    fun userLogin(email: String, password: String): String {
//
//        var responseString = ""
//
//        ApiInterface().userLogin(email, password).enqueue(object: retrofit2.Callback<ResponseBody> {
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e(TAG, t.toString())
//                responseString = t.toString()
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    responseString = response.body().toString()
//                } else {
//                    responseString = response.errorBody().toString()
//                }
//            }
//
//        })
//
//        return responseString
//    }

}