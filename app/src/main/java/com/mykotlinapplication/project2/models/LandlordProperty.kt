package com.mykotlinapplication.project2.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LandlordProperty(
    @SerializedName("id") var id: String,
    @SerializedName("propertyaddress") var address: String,
    @SerializedName("propertycity") var city: String,
    @SerializedName("propertystate") var state: String,
    @SerializedName("propertycountry") var country: String,
    @SerializedName("propertystatus") var status: String,
    @SerializedName("propertypurchaseprice") var price: String,
    @SerializedName("propertymortageinfo") var mortgageInfo: String,
    var image: String
): Parcelable