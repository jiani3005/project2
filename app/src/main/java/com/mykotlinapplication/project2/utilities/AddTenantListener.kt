package com.mykotlinapplication.project2.utilities

interface AddTenantListener {

    fun setNameError()
    fun setPhoneError(message: String)
    fun setEmailError(message: String)
    fun setAddressError()
    fun setCityError()
    fun setStateError()
    fun setPostcodeError(message: String)

}