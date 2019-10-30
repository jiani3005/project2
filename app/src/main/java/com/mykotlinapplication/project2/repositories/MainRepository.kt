package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.ApiInterface
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

object MainRepository {

    private val TAG = "MainRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val isUpdating = MutableLiveData<Boolean>()



}