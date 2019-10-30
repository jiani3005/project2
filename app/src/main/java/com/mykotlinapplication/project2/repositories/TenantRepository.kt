package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.models.ListingsPropertyList
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TenantRepository {

    private val TAG = "TenantRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val isUpdating = MutableLiveData<Boolean>()


    fun getListings(): MutableLiveData<ArrayList<ListingsProperty>> {
        isUpdating.value = true
        var propertyList = MutableLiveData<ArrayList<ListingsProperty>>()

        apiInterface.getListings().enqueue(object: Callback<ListingsPropertyList> {
            override fun onFailure(call: Call<ListingsPropertyList>, t: Throwable) {
                Log.e(TAG, "getListings() onFailure: $t")
                isUpdating.value = false
            }

            override fun onResponse(call: Call<ListingsPropertyList>, response: Response<ListingsPropertyList>) {
                if (response.isSuccessful) {
                    var result = response.body()!!
                    propertyList.value = result.listingsPropertyList
                    isUpdating.value = false
                } else {
                    Log.e(TAG, "getListings() response failure: ${response.errorBody()}")
                    isUpdating.value = false
                }
            }

        })

        return propertyList
    }

    fun getUserEmailAndType(): MutableLiveData<Pair<String, String>> {
        var userInfo = MutableLiveData<Pair<String, String>>()
        userInfo.value = Pair(sharedPreferences.getUserEmail(), sharedPreferences.getUserType())

        return userInfo
    }

    fun clearLoginSession() {
        SharedPreferencesManager.clearLoginSession()
    }

    fun getIsUpdating(): MutableLiveData<Boolean> {
        return isUpdating
    }

}