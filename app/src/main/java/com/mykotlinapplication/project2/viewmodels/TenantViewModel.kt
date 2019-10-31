package com.mykotlinapplication.project2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mykotlinapplication.project2.models.databases.ApiClient
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.repositories.TenantRepository

class TenantViewModel: ViewModel() {

    private val TAG = "TenantViewModel"
    private val repo = TenantRepository
    private var isUpdating = repo.getIsUpdating()

    private var selectedListings = MutableLiveData<ListingsProperty>()
    private var propertyList = repo.getListings()

    fun getListings(): LiveData<ArrayList<ListingsProperty>> {
        isUpdating = repo.getIsUpdating()
        propertyList.value = arrayListOf()
        propertyList = repo.getListings()
        return propertyList
    }

    fun setSelectedListings(listings: ListingsProperty) {
        selectedListings.value = listings
    }

    fun getSelectedListings(): LiveData<ListingsProperty> {
        return selectedListings
    }

    fun getUserEmailAndType(): LiveData<Pair<String, String>> {
        return repo.getUserEmailAndType()
    }

    fun clearLoginSession() {
        repo.clearLoginSession()
    }

    fun getIsUpdating(): MutableLiveData<Boolean> {
        return isUpdating
    }
}