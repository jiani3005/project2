package com.mykotlinapplication.project2.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.repositories.LandlordRepository
import com.mykotlinapplication.project2.utilities.AddPropertyListener
import com.mykotlinapplication.project2.utilities.AddTenantListener

class LandlordViewModel: ViewModel() {

    private val TAG = "LandlordViewModel"
    private val repo = LandlordRepository
    var addPropertyListener: AddPropertyListener? = null
    var addTenantListener: AddTenantListener? = null

    private var isUpdating = repo.getIsUpdating()
    private var property_list = repo.getProperty()
    private var tenant_list = repo.getTenants()
    private var selectedProperty = MutableLiveData<LandlordProperty>()
    private var selectedTenant = MutableLiveData<Tenant>()

    fun clearLoginSession() {
        LandlordRepository.clearLoginSession()
    }

    fun setSelectedProperty(landlordProperty: LandlordProperty) {
        selectedProperty.value = landlordProperty
    }

    fun getSelectedProperty(): LiveData<LandlordProperty> {
        return selectedProperty
    }

    fun setSelectedTenant(tenant: Tenant) {
        selectedTenant.value = tenant
    }

    fun getSelectedTenant(): LiveData<Tenant> {
        return selectedTenant
    }

    fun getProperty(): LiveData<ArrayList<LandlordProperty>> {
        property_list.value = arrayListOf()
        property_list = repo.getProperty()

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
//            isUpdating = repo.getIsUpdating()
            isSuccess = repo.addProperty(address, city, state, country, property_status, price, mortgageInfo)

        }

        return isSuccess
    }

    fun deleteProperty(): LiveData<Boolean> {
//        isUpdating = repo.getIsUpdating()
        return repo.deleteProperty(selectedProperty.value!!.id)
    }

//    fun updatePropertyList() {
//        var propertyArray = property_list.value!!
//        val index = propertyArray.indexOf(selectedProperty.value!!)
//        propertyArray.removeAt(index)
//
//        property_list.postValue(propertyArray)
//    }

    fun getTenants(): LiveData<ArrayList<Tenant>> {
//        isUpdating = repo.getIsUpdating()
        tenant_list.value = arrayListOf()
        tenant_list = repo.getTenants()

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

//            isUpdating = repo.getIsUpdating()
            isSuccess = repo.addTenant(capitalizeEachWord(name), email, fullAddress, phone, propertyId)
//            compositeDisposable.add(repo.addTenant(capitalizeEachWord(name), email, fullAddress, phone, propertyId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe({ d -> isUpdating.value = true })
//                .subscribe(
//                    { response ->
//                        val responseString = response.string()
//                        Log.d(TAG, "LandlordProperty ID# $propertyId, response.body() = $responseString")
//                        try {
//                            isSuccess.value = responseString.equals("successfully added")
//                        } catch (e: Exception) {
//                            Log.e(TAG, "addTenant() error: $e")
//                        }
//
//                    },
//                    { throwable -> Log.e(TAG, "addTenant() throwable: $throwable") }
//                ))


        }

        return isSuccess
    }

    fun getUserEmailAndType(): LiveData<Pair<String, String>> {
        return repo.getUserEmailAndType()
    }

    fun getIsUpdating(): LiveData<Boolean> {
        isUpdating = repo.getIsUpdating()
        return isUpdating
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