package com.mykotlinapplication.project2.utilities

interface RegisterListener {

    fun setEmailError(message: String)
    fun setPasswordError(message: String)
    fun setConfirmPasswordError(message: String)

}