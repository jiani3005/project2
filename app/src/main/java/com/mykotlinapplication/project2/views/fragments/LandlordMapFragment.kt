package com.mykotlinapplication.project2.views.fragments

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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

        landlordActivity.viewModel.getLocationsCoordinates().observe(landlordActivity, Observer { latLngList ->
            var latLng = LatLng(0.0, 0.0)

            for (e in latLngList) {
                latLng = e.third
                var marker = map.addMarker(MarkerOptions().position(latLng).title(e.second))
                marker.tag = Pair(e.first, e.second)
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

        var item = marker.tag as Pair<LandlordProperty, String>
        landlordActivity.viewModel.setSelectedProperty(item.first)
        landlordActivity.goToPropertyDetails()
    }



}