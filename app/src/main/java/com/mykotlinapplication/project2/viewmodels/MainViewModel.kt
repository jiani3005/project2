package com.mykotlinapplication.project2.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.mykotlinapplication.project2.utilities.LoginListener
import com.mykotlinapplication.project2.utilities.RegisterListener
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import com.mykotlinapplication.project2.utilities.ForgotPasswordListener
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel: ViewModel() {

//    var mainListener: MainListener? = null
    var loginListener: LoginListener?= null
    var registerListener: RegisterListener?= null
    var forgotPasswordListener: ForgotPasswordListener? = null
    private val TAG = "MainViewModel"
    private val apiInterface = ApiClient.getApiInterface()
    private val isUpdating = MutableLiveData<Boolean>()

    fun loadLoginInfo(): LiveData<ArrayList<Any>> {
        var info = MutableLiveData<ArrayList<Any>>()

        info.value = SharedPreferencesManager.getLoginInfo()

        return info
    }

    fun userLogin(email: String, password: String, rememberMe: Boolean): LiveData<Boolean?>{
//        var loginResponse = MutableLiveData<String>()
        var isTenant = MutableLiveData<Boolean>()
        isTenant.value = null
//        var isTenant = false

        if (rememberMe) {
            SharedPreferencesManager.setLoginInfo(email, password, rememberMe)
        } else {
            SharedPreferencesManager.setLoginInfo("", "", rememberMe)
        }

        if (email.isNullOrEmpty()) {
            loginListener?.setEmailError("Please enter your email")
        } else if (password.isNullOrEmpty()) {
            loginListener?.setPasswordError("Please enter your password")
        } else if (password.length < 8) {
            loginListener?.setPasswordError("Password must be at least length of 8")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginListener?.setEmailError("Invalid email")
        } else {
            isUpdating.value = true

            apiInterface.userLogin(email, password).enqueue(object: retrofit2.Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    loginListener?.onFailure("Account is suspended.\nPlease try again in 5 minutes")
                    isUpdating.value = false
                    Log.e(TAG, t.toString())
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                    if (response.isSuccessful) {
//                        Log.d(TAG, "loginResponse = ${response.body().toString()}")

                        if (response.body()!!.isJsonObject) {
                            val responseJsonObject = response.body()!!.asJsonObject

                            Log.d(TAG, responseJsonObject.toString())

                            try {
                                val message = responseJsonObject.get("msg").asString
                                if (message == "success") {

                                    val userId = responseJsonObject["userid"].asString
                                    val userType = responseJsonObject["usertype"].asString
                                    val userEmail = responseJsonObject["useremail"].asString
                                    val appApiKey = responseJsonObject["appapikey"].asString

                                    SharedPreferencesManager.setLoginSession(userId, userType, userEmail, appApiKey)

                                    loginListener?.onSuccess("Login success!")

                                    isTenant.value = userType == "tenant"

                                } else {
                                    val errorResponse = responseJsonObject["msg"].asJsonArray
                                    val returnCode = errorResponse[0].asInt
                                    if (returnCode == 0) {
                                        loginListener?.onFailure("Incorrect email/password.\n$returnCode attempt(s) left.\nPlease try again in 5 minutes.")

                                    } else {
                                        loginListener?.onFailure("Incorrect email/password.\n$returnCode attempt(s) left.")

                                    }
//                                    Log.d(TAG, "Login failed!. $returnCode attempt(s) left")
                                }

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
        return isTenant

    }

    fun userRegister (email: String, password: String, confirmPassword: String, isTenant: Boolean): LiveData<Boolean> {
        var registerResult = MutableLiveData<Boolean>()

        if (email.isNullOrEmpty()) {
            registerListener?.setEmailError("Please enter your email")
        } else if (password.isNullOrEmpty()) {
            registerListener?.setPasswordError("Please enter your password")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerListener?.setEmailError("Invalid email")
        } else if(password.length < 8) {
            registerListener?.setPasswordError("Password length must be at least 8")
        } else if (password != confirmPassword) {
            registerListener?.setConfirmPasswordError("Password mismatch")
        } else {
            var accountFor = "landlord"
            if (isTenant) {
                accountFor = "tenant"
            }

            isUpdating.value = true

            apiInterface.userRegister(email, email, password, accountFor).enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    registerListener?.onFailure("Register failed.\nEmail already exists.")
                    Log.e(TAG, "User register retrofit on failure: ${t.toString()}")
                    isUpdating.value = false
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (response.isSuccessful) {
                        val responseString = response.body()?.string()?.trim()

                        Log.d(TAG, "response string = $responseString")

                        if (responseString == "successfully registered") {
                            registerListener?.onSuccess("Register success!")
                            registerResult.value = true
                        } else {
                            registerListener?.onFailure("Register failed.\nEmail already exists.")
                        }

                        isUpdating.value = false

                    } else {
                        Log.e(TAG, "User register response failure")
                    }


                }

            })

        }

        return registerResult
    }

    fun userForgotPassword(email: String): LiveData<Boolean> {
        var result = MutableLiveData<Boolean>()

        if (email.isNullOrEmpty()) {
            forgotPasswordListener?.setEmailError("Please enter your registered email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotPasswordListener?.setEmailError("Invalid email")
        } else {
            isUpdating.value = true
            apiInterface.userForgotPassword(email).enqueue(object: retrofit2.Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Log.e(TAG, "ForgotPassword onFailure: $t")
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.isJsonObject) {
                            val responseJSONObject = response.body()!!.asJsonObject

                            Log.d(TAG, "response body = $responseJSONObject")

                            try {
                                if (responseJSONObject.get("msg").asString == "User Email and Password") {
                                    val userEmail = responseJSONObject.get("useremail").asString
                                    val password = responseJSONObject.get("userpassword").asString

                                    forgotPasswordListener?.onSuccess("$userEmail\n$password")
                                    result.value = true
//                                forgotPasswordListener?.hideProgressBar()

                                } else {
//                                forgotPasswordListener?.hideProgressBar()
                                    forgotPasswordListener?.onFailure("Email is not registered.")
                                }

                            } catch (e: Exception) {
                                Log.e(TAG, "response exception: $e")
                            }
                        } else {
                            Log.e(TAG, "response is not JsonObject")
                        }

                    } else {
                        Log.e(TAG, "ForgotPassword response failure")
                    }

                    isUpdating.value = false

                }

            })
        }

        return result
    }

    fun checkLoginSession(): LiveData<Boolean?> {
        var redirectToTenant = MutableLiveData<Boolean?>()
        redirectToTenant.value = null

        val response = SharedPreferencesManager.getLoginSession()

        if (response == "tenant") {
            redirectToTenant.value = true
        } else if (response == "landlord") {
            redirectToTenant.value = false
        } else {
            redirectToTenant.value = null
        }

        return redirectToTenant
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return isUpdating
    }

}