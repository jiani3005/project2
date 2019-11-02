package com.mykotlinapplication.project2.models

import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.mykotlinapplication.project2.MyApplication
import kotlin.Exception

class GeocoderAsync: AsyncTask<String, Unit, LatLng>() {

    private val TAG = "GeocoderAsync"

    override fun doInBackground(vararg params: String?): LatLng? {
        var fullAddress = params[0]
        var geocoderMatches: List<Address>? = null

        try {
            geocoderMatches = Geocoder(MyApplication.context).getFromLocationName(fullAddress, 1)
        } catch (e: Exception) {
            Log.e(TAG, "geomatcher failed: $e")
        }

        return if (geocoderMatches != null && geocoderMatches.isNotEmpty()) {
            LatLng(geocoderMatches[0].latitude, geocoderMatches[0].longitude)
        } else {
            null
        }


    }

}