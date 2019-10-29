package com.mykotlinapplication.project2.repositories

import androidx.lifecycle.MutableLiveData
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.SharedPreferencesManager

object TenantRepository {

    private val TAG = "TenantRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val isUpdating = MutableLiveData<Boolean>()


    fun clearLoginSession() {
        SharedPreferencesManager.clearLoginSession()
    }

}