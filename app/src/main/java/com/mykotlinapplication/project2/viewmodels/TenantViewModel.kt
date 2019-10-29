package com.mykotlinapplication.project2.viewmodels

import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import com.mykotlinapplication.project2.repositories.TenantRepository

class TenantViewModel: ViewModel() {

    private val TAG = "TenantViewModel"
    private val repo = TenantRepository
    private val apiInterface = ApiClient.getApiInterface()

    fun clearLoginSession() {
        repo.clearLoginSession()
    }

}