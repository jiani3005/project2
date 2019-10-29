package com.mykotlinapplication.project2.models

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val baseUrl = "http://rjtmobile.com/aamir/property-mgmt/"

    fun getApiInterface(): ApiInterface  {
//        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl).build().create(ApiInterface::class.java)
    }

}