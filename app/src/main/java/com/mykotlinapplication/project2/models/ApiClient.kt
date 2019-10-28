package com.mykotlinapplication.project2.models

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val baseUrl = "http://rjtmobile.com/aamir/property-mgmt/"

    fun getApiInterface(): ApiInterface  {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build().create(ApiInterface::class.java)
    }

}