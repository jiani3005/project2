package com.mykotlinapplication.project2.models

import com.google.gson.annotations.SerializedName

data class ListingsPropertyList (
    @SerializedName("Property") var listingsPropertyList: ArrayList<ListingsProperty>
)