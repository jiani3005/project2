package com.mykotlinapplication.project2.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tenant (
    @SerializedName("id") val id: String,
    @SerializedName("tenantname") val name: String,
    @SerializedName("tenantemail") val email: String,
    @SerializedName("tenantaddress") val address: String,
    @SerializedName("tenantmobile") val phone: String
): Parcelable