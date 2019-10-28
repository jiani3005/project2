package com.mykotlinapplication.project2.helpers

import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.models.Tenant

interface LandlordHelper {

    fun editAppBar(title: String, showBackButton: Boolean)
    fun goToProperty()
    fun goToPropertyDetails(property: Property)
    fun goToAddProperty()
    fun goToTenant()
    fun goToTenantDetails(tenant: Tenant)
    fun goToAddTenant(property: Property)
    fun goToMap()
    fun goToProfile()
    fun goToMainActivity()
}