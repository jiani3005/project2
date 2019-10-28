package com.mykotlinapplication.project2.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.repositories.LandlordRepository
import com.mykotlinapplication.project2.utilities.AddPropertyListener
import com.mykotlinapplication.project2.utilities.AddTenantListener

class LandlordViewModel: ViewModel() {

    private val TAG = "LandlordViewModel"
    private val isUpdating = MutableLiveData<Boolean>()
    private val repo = LandlordRepository
    var addPropertyListener: AddPropertyListener? = null
    var addTenantListener: AddTenantListener? = null
    private var selectedProperty = MutableLiveData<Property>()
    private var selectedTenant = MutableLiveData<Tenant>()
//    private val propertyList = LandlordRepository.getProperty()

    fun clearLoginSession() {
        LandlordRepository.clearLoginSession()
    }

    fun setSelectedProperty(property: Property) {
        selectedProperty.value = property
    }

    fun getSelectedProperty(): LiveData<Property> {
        return selectedProperty
    }

    fun setSelectedTenant(tenant: Tenant) {
        selectedTenant.value = tenant
    }

    fun getSelectedTenant(): LiveData<Tenant> {
        return selectedTenant
    }

    fun getProperty(): LiveData<ArrayList<Property>> {
        return repo.getProperty()
    }

    fun addProperty(address: String, city: String, state: String, country: String, property_status: String, price: String, mortgageInfo: String): LiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        if (address.isNullOrEmpty()) {
            addPropertyListener?.setAddressError()
        } else if (city.isNullOrEmpty()) {
            addPropertyListener?.setCityError()
        } else if (state.isNullOrEmpty()) {
            addPropertyListener?.setStateError()
        } else if (country.isNullOrEmpty()) {
            addPropertyListener?.setPostcodeError("Field must be filled")
        } else if (country.length < 5) {
            addPropertyListener?.setPostcodeError("Postcode must be length of 5")
        } else if (property_status.isNullOrEmpty()) {
            addPropertyListener?.setPropertyStatusError()
        } else if (price.isNullOrEmpty()) {
            addPropertyListener?.setPriceError()
        } else if (mortgageInfo.isNullOrEmpty()) {
            addPropertyListener?.setMortgageInfoError()
        } else {
            var latitude = ""
            var longitude = ""

            isUpdating.value = true

            isSuccess = repo.addProperty(address, city, state, country, property_status, price, mortgageInfo, latitude, longitude)

            isUpdating.value = false
        }

        return isSuccess
    }

    fun getTenants(): LiveData<ArrayList<Tenant>> {
        return repo.getTenants()
    }

    fun addTenant(name: String, phone: String, email: String, address: String, city: String, state: String, postcode: String, propertyId: String): LiveData<Boolean> {
        var isSuccess = MutableLiveData<Boolean>()

        if (name.isNullOrEmpty()) {
            addTenantListener?.setNameError()
        } else if (phone.isNullOrEmpty()) {
            addTenantListener?.setPhoneError("Field must be filled")
        } else if (phone.length != 10) {
            addTenantListener?.setPhoneError("Invalid phone number")
        } else if (email.isNullOrEmpty()) {
            addTenantListener?.setEmailError("Field must be filled")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            addTenantListener?.setEmailError("Invalid email")
        } else if (address.isNullOrEmpty()) {
            addTenantListener?.setAddressError()
        } else if (city.isNullOrEmpty()) {
            addTenantListener?.setCityError()
        } else if (state.isNullOrEmpty()) {
            addTenantListener?.setStateError()
        } else if (postcode.isNullOrEmpty()) {
            addTenantListener?.setPostcodeError("Field must be filled")
        } else if (postcode.length < 5) {
            addTenantListener?.setPostcodeError("Postcode must be length of 5")
        } else {
            var fullAddress = "$address\n$city, $state $postcode"

            isSuccess = repo.addTenant(name, email, fullAddress, phone, propertyId)
        }

        return isSuccess
    }

    fun getUserEmail(): LiveData<String> {
        return repo.getUserEmail()
    }


    fun getIsUpdating(): LiveData<Boolean> {
        return repo.getIsUpdating()
    }
}