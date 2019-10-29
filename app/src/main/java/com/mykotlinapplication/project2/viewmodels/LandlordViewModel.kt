package com.mykotlinapplication.project2.viewmodels

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.models.ApiClient
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.models.SharedPreferencesManager
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.repositories.LandlordRepository
import com.mykotlinapplication.project2.utilities.AddPropertyListener
import com.mykotlinapplication.project2.utilities.AddTenantListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import android.R.attr.password
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.maps.model.LatLng


class LandlordViewModel: ViewModel() {

    private val TAG = "LandlordViewModel"
    private val repo = LandlordRepository
    var addPropertyListener: AddPropertyListener? = null
    var addTenantListener: AddTenantListener? = null
    private var compositeDisposable = CompositeDisposable()

    private var isUpdating = repo.getIsUpdating()
    private var property_list = repo.getProperty()
    private var tenant_list = repo.getTenants()
    private var selectedProperty = MutableLiveData<Property>()
    private var selectedTenant = MutableLiveData<Tenant>()
    private var pendingProperty = MutableLiveData<Property>()
    private var pendingTenant = MutableLiveData<Tenant>()
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
        if (property_list.value == null) {
            property_list.postValue(repo.getProperty().value)
        }

        return property_list
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
            var geocoderMatches: List<Address>? = null

            try {
                var fullAddress = "$address, $city, $state $country"
                geocoderMatches = Geocoder(MyApplication.context).getFromLocationName(fullAddress, 1)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

            if (geocoderMatches != null) {
                latitude = geocoderMatches[0].latitude.toString()
                longitude = geocoderMatches[0].longitude.toString()
//                Log.d(TAG, "latitude = $latitude\nlongitude = $longitude")
            }

            pendingProperty.value = Property("", address, city, state, country, property_status, price, mortgageInfo)
            isSuccess = repo.addProperty(address, city, state, country, property_status, price, mortgageInfo, latitude, longitude)

        }

        return isSuccess
    }

    fun addPendingProperty() {
        isUpdating.value = true
        var propertyArray = property_list.value!!
        propertyArray.add(pendingProperty.value!!)
        property_list.postValue(propertyArray)
        isUpdating.value = false
    }

    fun deleteProperty(): LiveData<Boolean> {
        return repo.deleteProperty(selectedProperty.value!!.id)
    }

    fun deleteSuccessProperty() {
        var propertyArray = property_list.value!!
        val index = propertyArray.indexOf(selectedProperty.value!!)
        propertyArray.removeAt(index)

        property_list.postValue(propertyArray)
    }

    fun getTenants(): LiveData<ArrayList<Tenant>> {
        if (tenant_list.value == null) {
            tenant_list.postValue(repo.getTenants().value)
        }

        return tenant_list
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
            var fullAddress = "${capitalizeEachWord(address)}\n${capitalizeEachWord(city)}, ${state.toUpperCase()} $postcode"

//            isSuccess = repo.addTenant(capitalizeEachWord(name), email, fullAddress, phone, propertyId)
            compositeDisposable.add(repo.addTenant(capitalizeEachWord(name), email, fullAddress, phone, propertyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ d -> isUpdating.value = true })
                .subscribe(
                    { response ->
                        Log.d(TAG, "response.body() = ${response.string()}")
                        try {
                            if (response.string().toString().equals("successfully added")) {
                                isSuccess.value = true
                                Log.i(TAG, "isSuccess = ${isSuccess.value}")
                            } else {
                                isSuccess.value = false
                            }

                        } catch (e: Exception) {
                            Log.e(TAG, "addTenant() error: $e")
                        }

                    },
                    { throwable -> Log.e(TAG, "addTenant() throwable: $throwable") }
                ))


        }

        return isSuccess
    }

    fun addPendingTenant() {
//        isUpdating.value = true
        var tenantArray = tenant_list.value!!
        tenantArray.add(pendingTenant.value!!)
//        isUpdating.value = false
    }

    fun getUserEmail(): LiveData<String> {
        return repo.getUserEmail()
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return repo.getIsUpdating()
    }

    fun getLocationsCoordinates(): LiveData<ArrayList<Triple<Property, String, LatLng>>> {
        var result = MutableLiveData<ArrayList<Triple<Property, String, LatLng>>>()
        var latLngList = arrayListOf<Triple<Property, String, LatLng>>()

        for (e in property_list.value!!) {
//            var latitude = ""
//            var longitude = ""
            var fullAddress = ""
            var formattedAddress = ""
            var geocoderMatches: List<Address>? = null

            try {
                formattedAddress = "${e.address}\n${e.city}, ${e.state} ${e.country}"
                fullAddress = "${e.address}, ${e.city}, ${e.state} ${e.country}"
                geocoderMatches = Geocoder(MyApplication.context).getFromLocationName(fullAddress, 1)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

            if (geocoderMatches != null) {
                var latitude = geocoderMatches[0].latitude
                var longitude = geocoderMatches[0].longitude
                latLngList.add(Triple(e, formattedAddress, LatLng(latitude, longitude)))
            }
        }
        result.value = latLngList

        return result
    }

    private fun capitalizeEachWord(string: String): String {
        var inputList = string.split(" ")
        var outputString = ""

        for (e in inputList) {
            outputString += e.capitalize() + " "
        }

        return outputString.trim()
    }
}