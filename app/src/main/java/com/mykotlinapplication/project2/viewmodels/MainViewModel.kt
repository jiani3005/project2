package com.mykotlinapplication.project2.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.utilities.LoginListener
import com.mykotlinapplication.project2.utilities.RegisterListener
import com.mykotlinapplication.project2.repositories.MainRepository
import com.mykotlinapplication.project2.utilities.ForgotPasswordListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel"
    private val repo = MainRepository
    private var isUpdating = repo.getIsUpdating()
    private val compositeDisposable = CompositeDisposable()
    private var forgotPasswordInfo = MutableLiveData<Pair<String, String>>()

    var loginListener: LoginListener?= null
    var registerListener: RegisterListener?= null
    var forgotPasswordListener: ForgotPasswordListener? = null

    fun loadLoginInfo(): LiveData<ArrayList<Any>> {
        return repo.loadLoginInfo()
    }

    fun userLogin(email: String, password: String, rememberMe: Boolean): LiveData<Map<String, String>> {
//        var loginResponse = MutableLiveData<String>()
        var returnResult = MutableLiveData<Map<String, String>>()

        if (rememberMe) {
            repo.setLoginInfo(email, password, rememberMe)
        } else {
            repo.setLoginInfo("", "", rememberMe)
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
            isUpdating = repo.getIsUpdating()

            returnResult = repo.userLogin(email, password)

        }
        return returnResult

    }

    fun userRegister (email: String, password: String, confirmPassword: String, isTenant: Boolean): LiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

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

            isUpdating = repo.getIsUpdating()
            isSuccess = repo.userRegister(email, password, accountFor)

        }

        return isSuccess
    }

    fun userForgotPassword(email: String): LiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        if (email.isNullOrEmpty()) {
            forgotPasswordListener?.setEmailError("Please enter your registered email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotPasswordListener?.setEmailError("Invalid email")
        } else {
//            message = repo.userForgotPassword(email)
            isUpdating.value = true
            compositeDisposable.add(repo.userForgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        val responseJsonElement = response

                        if (responseJsonElement.isJsonObject) {
                            val responseJSONObject = responseJsonElement.asJsonObject

                            Log.d(TAG, "response body = $responseJSONObject")

                            try {
                                if (responseJSONObject.get("msg").asString == "User Email and Password") {
                                    val userEmail = responseJSONObject.get("useremail").asString
                                    val password = responseJSONObject.get("userpassword").asString

//                                    result = "$userEmail\n$password"
                                    forgotPasswordInfo.value = Pair(userEmail, password)
//                                    Log.d(TAG, "forgot password info set!")
                                    isSuccess.value = true


                                } else {
                                    isSuccess.value = false
                                }
                                isUpdating .postValue(false)
                            } catch (e: Exception) {
                                Log.e(TAG, "response exception: $e")
                            }
                        } else {
                            Log.e(TAG, "response is not JsonObject")
                        }

                    }, {throwable ->
                        Log.e(TAG, "userForgotPassword() throwable: $throwable")
                    }))

        }

        return isSuccess
    }

    fun checkLoginSession(): LiveData<Boolean?> {
        return repo.checkLoginSession()
    }

    fun getForgotPasswordInfo(): Pair<String, String> {
//        Log.d(TAG, "email = ${forgotPasswordInfo.value?.first}")
        return forgotPasswordInfo.value!!
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return repo.getIsUpdating()
    }

}