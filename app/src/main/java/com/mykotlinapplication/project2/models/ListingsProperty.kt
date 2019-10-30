package com.mykotlinapplication.project2.models

import com.google.gson.annotations.SerializedName

data class ListingsProperty (
    @SerializedName("id") var id: String,
    @SerializedName("propertyaddress") var address: String,
    @SerializedName("propertycity") var city: String,
    @SerializedName("propertystate") var state: String,
    @SerializedName("propertycountry") var postcode: String,
    @SerializedName("propertystatus") var propertyStatus: String,
    @SerializedName("propertypurchaseprice") var price: String,
    @SerializedName("propertymortageinfo") var mortgageInfo: String,
    @SerializedName("propertyuserid") var landlordId: String,
    @SerializedName("propertylatitude") var latitude: String,
    @SerializedName("propertylongitude") var longitude: String
)