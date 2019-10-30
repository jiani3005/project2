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
import com.mykotlinapplication.project2.repositories.MainRepository
import com.mykotlinapplication.project2.utilities.ForgotPasswordListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel"
    private val repo = MainRepository
    private val apiInterface = ApiClient.getApiInterface()
    private var isUpdating = repo.getIsUpdating()
    private val compositeDisposable = CompositeDisposable()

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
//            isUpdating = repo.getIsUpdating()

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

//            isUpdating = repo.getIsUpdating()
            isSuccess = repo.userRegister(email, password, accountFor)

        }

        return isSuccess
    }

    fun userForgotPassword(email: String): LiveData<String> {
        var message = MutableLiveData<String>()

        if (email.isNullOrEmpty()) {
            forgotPasswordListener?.setEmailError("Please enter your registered email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotPasswordListener?.setEmailError("Invalid email")
        } else {
//            isUpdating = repo.getIsUpdating()
            message = repo.userForgotPassword(email)

        }

        return message
    }

    fun checkLoginSession(): LiveData<Boolean?> {
        return repo.checkLoginSession()
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return repo.getIsUpdating()
    }

}