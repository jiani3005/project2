package com.mykotlinapplication.project2.utilities

import androidx.lifecycle.LiveData

interface LoginListener {

    fun setEmailError(message: String)
    fun setPasswordError(message: String)
    fun onFailure(message: String)
    fun onSuccess(message: String)
    fun hideProgressBar()
    fun showProgressBar()

}