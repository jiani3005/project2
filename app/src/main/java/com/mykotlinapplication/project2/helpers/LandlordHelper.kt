package com.mykotlinapplication.project2.helpers

import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.models.Tenant

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

}