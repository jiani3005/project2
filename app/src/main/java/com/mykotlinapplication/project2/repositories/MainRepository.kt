package com.mykotlinapplication.project2.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.JsonElement
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.models.databases.ApiClient
import com.mykotlinapplication.project2.models.databases.FirebaseAuthManager
import com.mykotlinapplication.project2.models.databases.SharedPreferencesManager
import com.mykotlinapplication.project2.views.activities.MainActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository {

    private const val TAG = "MainRepository"
    private val sharedPreferences = SharedPreferencesManager
    private val apiInterface = ApiClient.getApiInterface()
    private val firebaseAuthManager = FirebaseAuthManager
    private val isUpdating = MutableLiveData<Boolean>()
//    private val isGoogleSignInSuccess = MutableLiveData<Boolean>()
    private val disposables = CompositeDisposable()
    private val googleSignInResult = MutableLiveData<Map<String, String>>()

    fun loadLoginInfo(): MutableLiveData<ArrayList<Any>> {
        var info = MutableLiveData<ArrayList<Any>>()
        info.value = sharedPreferences.getLoginInfo()
        return info
    }

    fun setLoginInfo(email: String, password: String, rememberMe: Boolean) {
        sharedPreferences.setLoginInfo(email, password, rememberMe)
    }

    fun checkLoginSession(): MutableLiveData<Boolean?> {
        var redirectToTenant = MutableLiveData<Boolean?>()
        redirectToTenant.value = null

        val response = sharedPreferences.getLoginSession()

        when (response) {
            "tenant" -> redirectToTenant.value = true
            "landlord" -> redirectToTenant.value = false
            else -> redirectToTenant.value = null
        }

        return redirectToTenant
    }

    fun userRegister(email: String, password: String, accountFor: String): MutableLiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        apiInterface.userRegister(email, email, password, accountFor).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isSuccess.value = false
                Log.e(TAG, "userRegister() onFailure: $t")
                isUpdating.value = false
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val responseString = response.body()?.string()?.trim()

                    Log.d(TAG, "response string = $responseString")

                    isSuccess.value = responseString == "successfully registered"

                    isUpdating.value = false

                } else {
                    Log.e(TAG, "User register response failure")
                }


            }

        })
        return isSuccess
    }

    fun userForgotPassword(email: String): Single<JsonElement> {
        return apiInterface.userForgotPassword(email)
    }

    fun userLogin(email: String, password: String): MutableLiveData<Map<String, String>> {
//        var isTenant = MutableLiveData<Boolean?>()
        var returnResult = MutableLiveData<Map<String, String>>()
        var result = mutableMapOf<String, String>()

        isUpdating.value = true
        apiInterface.userLogin(email, password).enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                result["isSuccess"] = "false"
                result["msg"] = "Account is suspended.\nPlease try again in 5 minutes"
                returnResult.value = result
                isUpdating.value = false
                Log.e(TAG, t.toString())
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful) {
                        Log.d(TAG, "loginResponse = ${response.body().toString()}")

                    if (response.body()!!.isJsonObject) {
                        val responseJsonObject = response.body()!!.asJsonObject

//                        Log.d(TAG, responseJsonObject.toString())

                        try {
                            val message = responseJsonObject.get("msg").asString
                            if (message == "success") {

                                val userId = responseJsonObject["userid"].asString
                                val userType = responseJsonObject["usertype"].asString
                                val userEmail = responseJsonObject["useremail"].asString
                                val appApiKey = responseJsonObject["appapikey"].asString

                                sharedPreferences.setLoginSession(userId, userType, userEmail, appApiKey)

                                result["isSuccess"] = "true"
                                result["msg"] = userType

//                                loginListener?.onSuccess("Login success!")
//
//                                isTenant.value = userType == "tenant"

                            } else {
                                val errorResponse = responseJsonObject["msg"].asJsonArray
                                val returnCode = errorResponse[0].asString

                                when (returnCode) {
                                    "Email is not register" -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "Email is not registered"

                                    }
                                    "0" -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "Incorrect email/password.\n$returnCode attempt(s) left.\nPlease try again in 5 minutes."

                            //                                    loginListener?.onFailure("Incorrect email/password.\n$returnCode attempt(s) left.\nPlease try again in 5 minutes.")

                                    }
                                    else -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "Incorrect email/password.\n" + "$returnCode attempt(s) left."
                            //                                    loginListener?.onFailure("Incorrect email/password.\n$returnCode attempt(s) left.")

                                    }
                                }
//                                    Log.d(TAG, "Login failed!. $returnCode attempt(s) left")
                            }
                            returnResult.value = result
                            isUpdating.value = false

                        } catch (e: Exception) {
//                                Log.d(TAG, "Account is suspended. Please try again in 5 minutes")
                            Log.e(TAG, e.toString())
                        }
                    } else {
                        Log.e(TAG, "Response is not JSON element!")
                    }

                } else {
                    Log.e(TAG, "loginResponseError = ${response.errorBody()}")
                }

            }
        })

        return returnResult
    }

    fun userGoogleLogin(gooleAcct: GoogleSignInAccount) {
        isUpdating.value = true
        var result = mutableMapOf<String, String>()

        disposables.add(firebaseAuthManager.userLogin(gooleAcct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ // receive success callback
                Log.d(TAG, "current user = ${FirebaseAuth.getInstance().currentUser!!.uid}")
                loginGoogleUserOnApi()
//                result["isSuccess"] = "true"
//                result["msg"] = "Login success!"
//                isGoogleSignInSuccess.value = true
//                isUpdating.value = false
//                googleSignInResult.value = result
            }, { throwable ->
                result["isSuccess"] = "false"
                result["msg"] = "Login failed"
                isUpdating.value = false
                googleSignInResult.value = result
                Log.e(TAG, "userGoogleLogin() throwable: $throwable")
            }))

    }

    private fun loginGoogleUserOnApi() {
        var googleUser = FirebaseAuth.getInstance().currentUser!!
        var result = mutableMapOf<String, String>()

        isUpdating.value = true
        apiInterface.userLogin(googleUser.email!!, googleUser.email!!).enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                result["isSuccess"] = "false"
                result["msg"] = "Account is suspended.\nPlease try again in 5 minutes"
                googleSignInResult.value = result
                isUpdating.value = false
                Log.e(TAG, t.toString())
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful) {
                    Log.d(TAG, "loginResponse = ${response.body().toString()}")

                    if (response.body()!!.isJsonObject) {
                        val responseJsonObject = response.body()!!.asJsonObject

//                        Log.d(TAG, responseJsonObject.toString())

                        try {
                            val message = responseJsonObject.get("msg").asString
                            if (message == "success") {

                                val userId = responseJsonObject["userid"].asString
                                val userType = responseJsonObject["usertype"].asString
                                val userEmail = responseJsonObject["useremail"].asString
                                val appApiKey = responseJsonObject["appapikey"].asString

                                sharedPreferences.setLoginSession(userId, userType, userEmail, appApiKey)

                                result["isSuccess"] = "true"
                                result["msg"] = userType

//                                loginListener?.onSuccess("Login success!")
//
//                                isTenant.value = userType == "tenant"

                            } else {
                                val errorResponse = responseJsonObject["msg"].asJsonArray
                                val returnCode = errorResponse[0].asString

                                when (returnCode) {
                                    "Email is not register" -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "New User"
                                    }
                                    "0" -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "Incorrect email/password.\n$returnCode attempt(s) left.\nPlease try again in 5 minutes."
                                    }
                                    else -> {
                                        result["isSuccess"] = "false"
                                        result["msg"] = "Incorrect email/password.\n$returnCode attempt(s) left."
                                    }

                                }

//                                    Log.d(TAG, "Login failed!. $returnCode attempt(s) left")
                            }
                            googleSignInResult.value = result
                            isUpdating.value = false

                        } catch (e: Exception) {
//                                Log.d(TAG, "Account is suspended. Please try again in 5 minutes")
                            Log.e(TAG, e.toString())
                        }
                    } else {
                        Log.e(TAG, "Response is not JSON element!")
                    }

                } else {
                    Log.e(TAG, "loginResponseError = ${response.errorBody()}")
                }

            }
        })


    }

    fun registerGoogleUserOnApi(userType: String) {
        var googleUserEmail = FirebaseAuth.getInstance().currentUser!!.email!!
        var result = mutableMapOf<String, String>()
        isUpdating.value = true

        apiInterface.userRegister(googleUserEmail, googleUserEmail, googleUserEmail, userType).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result["isSuccess"] = "false"
                result["msg"] = "register error"

                googleSignInResult.value = result
                isUpdating.value = false

                Log.e(TAG, "userRegister() onFailure: $t")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val responseString = response.body()?.string()?.trim()

                    Log.d(TAG, "response string = $responseString")

                    if (responseString == "successfully registered") {
                        loginGoogleUserOnApi()
                    } else {
                        result["isSuccess"] = "false"
                        result["msg"] = "register error"
                        googleSignInResult.value = result
                    }

                    isUpdating.value = false

                } else {
                    Log.e(TAG, "User register response failure")
                }


            }

        })

    }

    fun getGoogleSignInResult(): MutableLiveData<Map<String, String>> {
        return googleSignInResult
    }

    fun getIsUpdating(): MutableLiveData<Boolean> {
        return isUpdating
    }

}