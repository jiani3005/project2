package com.mykotlinapplication.project2.views.fragments

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentMapBinding
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.views.activities.LandlordActivity
import com.mykotlinapplication.project2.views.adapters.MapInfoWindowAdapter
import kotlinx.android.synthetic.main.property_map_info_content.view.*
import kotlinx.android.synthetic.main.property_map_info_window.view.*
import java.lang.Exception

class LandlordMapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var landlordActivity: LandlordActivity
    private lateinit var map: GoogleMap
    private val TAG = "LandlordMapFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        landlordActivity.editAppBar("Map", true)

        landlordActivity.viewModel.getIsUpdating().observe(landlordActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBarLayout.visibility = View.VISIBLE
            } else {
                binding.progressBarLayout.visibility = View.GONE
            }
        })


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        binding.progressBarLayout.visibility = View.VISIBLE
        map = googleMap

        with(map) {
            setInfoWindowAdapter(MapInfoWindowAdapter(this@LandlordMapFragment))
            setOnMarkerClickListener(this@LandlordMapFragment)
            setOnInfoWindowClickListener(this@LandlordMapFragment)
        }

        val apolis = LatLng(41.917300, -88.264400)

        landlordActivity.viewModel.getProperty().observe(landlordActivity, Observer { propertyList ->
            for (e in propertyList) {
                try {
                    var latLng = LatLng(e.latitude.toDouble(), e.longitude.toDouble())
                    var fullAddress = capitalizeEachWord(e.address) + "\n" + capitalizeEachWord(e.city) + ", " + e.state.toUpperCase() + " " + e.country
                    var marker = map.addMarker(MarkerOptions().position(latLng).title(fullAddress))
                    marker.tag = e
                } catch (e: Exception) {
                    Log.e(TAG, "Fail to add tag: $e")
                }
            }
        })
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(apolis, 10.0f))
        binding.progressBarLayout.visibility = View.GONE
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.zIndex += 1.0f
        return false
    }

    override fun onInfoWindowClick(marker: Marker) {

        var item = marker.tag as LandlordProperty
        landlordActivity.viewModel.setSelectedProperty(item)
        landlordActivity.goToPropertyDetails()
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