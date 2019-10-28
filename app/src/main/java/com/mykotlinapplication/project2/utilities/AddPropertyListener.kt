package com.mykotlinapplication.project2.utilities

interface AddPropertyListener {

    fun setAddressError()
    fun setCityError()
    fun setStateError()
    fun setPostcodeError(message: String)
    fun setPropertyStatusError()
    fun setPriceError()
    fun setMortgageInfoError()

}