package com.mykotlinapplication.project2.utilities

interface ForgotPasswordListener {

    fun setEmailError(message: String)
    fun onFailure(message: String)
    fun onSuccess(message: String)
    fun hideProgressBar()
    fun showProgressBar()

}