package com.mykotlinapplication.project2.helpers

interface MainHelper {

    fun goToLogin()
    fun goToRegister()
    fun goToForgotPassword()
    fun goToTenantActivity()
    fun goToLandlordActivity()
    fun goToTermsAndConditions()
    fun setUpNotification()
    fun loginWithGoogle()
    fun loginWithFacebook()
    fun showAlertDialog()
    fun showSnackbar(message: String)

}