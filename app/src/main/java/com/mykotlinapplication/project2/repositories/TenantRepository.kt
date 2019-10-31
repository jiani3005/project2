package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mykotlinapplication.project2.models.*
import com.mykotlinapplication.project2.models.databases.ApiClient
import com.mykotlinapplication.project2.models.databases.ImageDatabase
import com.mykotlinapplication.project2.models.databases.SharedPreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

object TenantRepository {

    private val TAG = "TenantRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val imageDatabase = ImageDatabase.getImageDatabaseInstance().imageDao()
    private val isUpdating = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    private val propertyImages = imageDatabase.getImageFromCategory("property")


    fun getListings(): MutableLiveData<ArrayList<ListingsProperty>> {
        isUpdating.value = true
        var propertyList = MutableLiveData<ArrayList<ListingsProperty>>()
        var newList = arrayListOf<ListingsProperty>()

        compositeDisposable.add(apiInterface.getListings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    val propertyArray = list.listingsPropertyList
                    for (e in propertyArray) {
                        if (e.price != "" && e.address != "" && e.city != "" && e.state != "" && e.postcode != "") {
                            if (e.price.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                                if (e.address.toLowerCase() != "abc" && e.city.toLowerCase() != "abc" && e.state.toLowerCase() != "abc" && e.postcode.toLowerCase() != "abc") {
                                    e.image = propertyImages[Random.nextInt(propertyImages.size)].imageLink
                                    newList.add(e)
                                }
                            }

                        }
                    }
                    propertyList.value = newList
                    isUpdating.value = false
                }, { throwable ->
                    Log.e(TAG, "getListings() throwable: $throwable")
                    isUpdating.value = false
                }))


//        apiInterface.getListings().enqueue(object: Callback<ListingsPropertyList> {
//            override fun onFailure(call: Call<ListingsPropertyList>, t: Throwable) {
//                Log.e(TAG, "getListings() onFailure: $t")
//                isUpdating.value = false
//            }
//
//            override fun onResponse(call: Call<ListingsPropertyList>, response: Response<ListingsPropertyList>) {
//                if (response.isSuccessful) {
//                    var result = response.body()!!
//                    propertyList.value = result.listingsPropertyList
//                    isUpdating.value = false
//                } else {
//                    Log.e(TAG, "getListings() response failure: ${response.errorBody()}")
//                    isUpdating.value = false
//                }
//            }
//
//        })

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