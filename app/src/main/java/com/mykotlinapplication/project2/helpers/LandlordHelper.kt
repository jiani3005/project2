package com.mykotlinapplication.project2.helpers

interface LandlordHelper {

    fun editAppBar(title: String, showBackButton: Boolean)
    fun goToProperty()
    fun goToPropertyDetails()
    fun goToAddProperty()
    fun deleteProperty()
    fun goToTenant()
    fun goToTenantDetails()
    fun goToAddTenant()
    fun goToMap()
    fun goToProfile()
    fun goToMainActivity()
    fun shareProperty()
    fun performCommunicationAction(title: String, message: String, action: String)

}