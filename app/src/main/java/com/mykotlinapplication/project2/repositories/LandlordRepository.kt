package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import com.mykotlinapplication.project2.models.Tenant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object LandlordRepository {

    private val TAG = "LandlordRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val isUpdating = MutableLiveData<Boolean>()
    private val testingId = "3"

    fun getProperty(): MutableLiveData<ArrayList<LandlordProperty>> {
        var propertyList = MutableLiveData<ArrayList<LandlordProperty>>()
        isUpdating.value = true

        apiInterface.getLandlordProperty(testingId, sharedPreferences.getUserType()).enqueue(object: retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                isUpdating.value = false
                Log.d(TAG, "getProperty onFailure: $t")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//                Log.d(TAG, "responseBody = ${response.body()}")

                if (response.isSuccessful) {

                    if (response.body()!!.isJsonObject) {
                        try {
                            val responseJsonObject = response.body()!!.asJsonObject
                            val propertyJsonArray = responseJsonObject.getAsJsonArray("Property")

                            val propertyArray = arrayListOf<LandlordProperty>()
                            for (i in 0 until propertyJsonArray.size()) {
                                var property = propertyJsonArray[i].asJsonObject

                                var gson = Gson()
                                propertyArray.add(gson.fromJson(property, LandlordProperty::class.java))

//                                var id = property["id"].asString
//                                var address = property["propertyaddress"].asString.capitalize()
//                                var city = property["propertycity"].asString.capitalize()
//                                var state = property["propertystate"].asString.capitalize()
//                                var country = property["propertycountry"].asString.capitalize()
//                                var status = property["propertystatus"].asString.capitalize()
//                                var price = property["propertypurchaseprice"].asString
//                                var mortgageInfo = property["propertymortageinfo"].asString.capitalize()
//
//                                propertyArray.add(LandlordProperty(id, address, city, state, country, status, price, mortgageInfo))
//                                Log.i(TAG, "${LandlordProperty(id, address, city, state, country, status, price)}")
                            }

                            propertyList.value = propertyArray

                        } catch (e: Exception) {
                            Log.e(TAG, "transformation failed")
                        }
                    }

                } else {
                    Log.e(TAG, "getProperty response failure: ${response.errorBody()}")
                }
                isUpdating.value = false
            }

        })

        return propertyList
    }

    fun addProperty(address: String, city: String, state: String, country: String, property_status: String, price: String, mortgageInfo: String, latitude: String, longitude: String): MutableLiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        val userId = testingId
        val userType = sharedPreferences.getUserType()

        isUpdating.value = true

        apiInterface.addProperty(address, city, state, country, property_status, price, mortgageInfo, userId, userType, latitude, longitude).enqueue(object: retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e(TAG, "addProperty() onFailure: $t")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {

                    Log.d(TAG, "response = ${response.body()}")

                    if (response.body()!!.isJsonObject) {
                        try {
                            val responseJsonObject = response.body()!!.asJsonObject
                            val msgArray = responseJsonObject["msg"].asJsonArray

                            isSuccess.value = msgArray[0].asString == "successfully added"

                        } catch (e: Exception) {
                            Log.e(TAG,"addProperty() failed to get result")
                        }
                    }

                } else {
                    Log.e(TAG, "addProperty() response failure: ${response.errorBody()}")
                }
                isUpdating.value = false
            }
        })

        return isSuccess
    }

    fun deleteProperty(propertyId: String): MutableLiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        isUpdating.value = true
        apiInterface.deleteProperty(propertyId).enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e(TAG, "deleteProperty() onFailure: $t")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {

                    if (response.body()!!.isJsonObject) {
                        try {
                            val responseJsonObject = response.body()!!.asJsonObject
                            val msgArray = responseJsonObject["msg"].asJsonArray

                            Log.i(TAG, msgArray[0].asString)

                            isSuccess.value = msgArray[0].asString == "deleted property succesfully"


                        } catch (e: Exception) {
                            Log.e(TAG, "deleteProperty() convert response failure: $e")
                        }
                    } else {
                        Log.e(TAG, "deleteProperty() convert response failure!")
                    }

                    isUpdating.value = false
                } else {
                    Log.e(TAG, "deleteProperty() response failure: ${response.errorBody()}")
                }
            }

        })

        return isSuccess

    }

    fun getTenants(): MutableLiveData<ArrayList<Tenant>> {
        var tenants = MutableLiveData<ArrayList<Tenant>>()

        isUpdating.value = true
        apiInterface.getTenants(testingId).enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e(TAG, "getTenants() onFailure: $t")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {

                    if (response.body()!!.isJsonObject) {
                        val responseJsonObject = response.body()!!.asJsonObject

                        try {
                            val jsonArrayTenant = responseJsonObject.getAsJsonArray("Tenants")

                            var tenantArrayList = arrayListOf<Tenant>()
                            for (i in 0 until jsonArrayTenant.size()) {
                                var tenant = jsonArrayTenant[i].asJsonObject

                                var gson = Gson()
                                tenantArrayList.add(gson.fromJson(tenant, Tenant::class.java))

//                                var id = tenant["id"].asString
//                                var name = tenant["tenantname"].asString
//                                var email = tenant["tenantemail"].asString
//                                var address = tenant["tenantaddress"].asString
//                                var phone = tenant["tenantmobile"].asString
//
//                                tenantArrayList.add(Tenant(id, name, email, address, phone))
                            }
                            tenants.value = tenantArrayList

                        } catch (e: Exception) {
                            Log.e(TAG, "getTenants() exception thrown: $e")
                        }


                    } else {
                        Log.e(TAG, "getTenants() fail to get data")
                    }

                } else {
                    Log.e(TAG, "getTenants() response failure: ${response.errorBody()}")
                }
                isUpdating.value = false
            }

        })


        return tenants
    }

    fun addTenant(name: String, email: String, address: String, phone: String, propertyId: String): MutableLiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        isUpdating.value = true
        apiInterface.addTenant(name, email, address, phone, propertyId, testingId).enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "addTenant() onFailure: $t")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseString = response.body()!!.string()
                        Log.d(TAG, responseString)

                        if ("successfully added".equals(responseString)) {
                            isSuccess.value = true
                            Log.d(TAG, "isSuccess = ${isSuccess.value}")
                        } else {
                            isSuccess.value = false
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "addTenant() convert response failure!: $e ")
                    }
                    isUpdating.value = false
                } else {
                    Log.e(TAG, "addTenant() response failure: ${response.errorBody()}")
                }

            }

        })
        return isSuccess
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