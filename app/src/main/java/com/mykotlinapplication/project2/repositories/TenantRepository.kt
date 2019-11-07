package com.mykotlinapplication.project2.repositories

import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.models.*
import com.mykotlinapplication.project2.models.databases.ApiClient
import com.mykotlinapplication.project2.models.databases.FirebaseAuthManager
import com.mykotlinapplication.project2.models.databases.ImageDatabase
import com.mykotlinapplication.project2.models.databases.SharedPreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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

        CoroutineScope(IO).launch {
            try {
                val response = apiInterface.getListings()

                CoroutineScope(Main).launch {
                    propertyList.value = locationListValidation(response.listingsPropertyList)
                }

            } catch (e: Exception) {
                Log.e(TAG, "getListings(): $e")
            }
        }

        propertyList.value = newList
        isUpdating.value = false


//        compositeDisposable.add(apiInterface.getListings()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ list ->
//                    val propertyArray = list.listingsPropertyList
//
//                    CoroutineScope(Main).launch {
//                        propertyList.value = locationListValidation(propertyArray)
//                    }
//
//
//                    propertyList.value = newList
//                    isUpdating.value = false
//                }, { throwable ->
//                    Log.e(TAG, "getListings() throwable: $throwable")
//                    isUpdating.value = false
//                }))


        return propertyList
    }

    fun getUserEmailAndType(): MutableLiveData<Pair<String, String>> {
        var userInfo = MutableLiveData<Pair<String, String>>()
        userInfo.value = Pair(sharedPreferences.getUserEmail(), sharedPreferences.getUserType())

        return userInfo
    }

    fun clearLoginSession() {
        if (sharedPreferences.getIsFirebaseUser()) {
            Log.d(TAG, "Signing out Firebase user")
            FirebaseAuthManager.signOutUser()
        }
        sharedPreferences.clearLoginSession()

    }

    fun getIsUpdating(): MutableLiveData<Boolean> {
        return isUpdating
    }

    private fun isLocationValid(fullAddress: String): LatLng? {
        var geocoderMatches: List<Address>? = null

        try {
            geocoderMatches = Geocoder(MyApplication.context).getFromLocationName(fullAddress, 1)
        } catch (e: Exception) {
            Log.e(TAG, "Geomatcher failed: $e")
        }

        return if (geocoderMatches != null && geocoderMatches.isNotEmpty()) {
            LatLng(geocoderMatches[0].latitude, geocoderMatches[0].longitude)
        } else {
            null
        }
    }

    private suspend fun locationListValidation(propertyList: ArrayList<ListingsProperty>): ArrayList<ListingsProperty> {
        var newList = arrayListOf<ListingsProperty>()

        isUpdating.value = true

        withContext(IO) {
            for (i in propertyList.indices) {
                if (propertyList[i].price != "" && propertyList[i].address != "" && propertyList[i].city != "" && propertyList[i].state != "" && propertyList[i].postcode != "" && propertyList[i].latitude != "" && propertyList[i].latitude != "") {
                    if (propertyList[i].price.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                        if (propertyList[i].address.toLowerCase() != "abc" && propertyList[i].city.toLowerCase() != "abc" && propertyList[i].state.toLowerCase() != "abc" && propertyList[i].postcode.toLowerCase() != "abc") {

                            var fullAddress = "${propertyList[i].address}, ${propertyList[i].city}, ${propertyList[i].state} ${propertyList[i].postcode}"

                            var location = isLocationValid(fullAddress)

                            if (location != null) {
                                propertyList[i].image = propertyImages[i % propertyImages.size].imageLink
                                newList.add(propertyList[i])
//                                Log.d(TAG, "valid property = ${propertyList[i]}")
                            }

                        }
                    }

                }
            }
        }

        isUpdating.value = false


        return newList
    }



}