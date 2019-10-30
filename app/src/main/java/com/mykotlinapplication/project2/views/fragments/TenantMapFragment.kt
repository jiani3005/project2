package com.mykotlinapplication.project2.views.fragments

import android.Manifest
import android.content.Context
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
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.views.activities.TenantActivity
import com.mykotlinapplication.project2.views.adapters.MapInfoWindowAdapter
import java.lang.Exception

class TenantMapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private val TAG = "TenantMapFragment"
    private lateinit var binding: FragmentMapBinding
    private lateinit var tenantActivity: TenantActivity
    private lateinit var map: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tenantActivity = context as TenantActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        tenantActivity.editAppBar("Map", true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tenantActivity.viewModel.getIsUpdating().observe(tenantActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBarLayout.visibility = View.VISIBLE
            } else {
                binding.progressBarLayout.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true


        val apolis = LatLng(41.917300, -88.264400)

        with(map) {
            setOnInfoWindowClickListener(this@TenantMapFragment)
            setInfoWindowAdapter(MapInfoWindowAdapter(this@TenantMapFragment))
            setOnMarkerClickListener(this@TenantMapFragment)
        }

        tenantActivity.viewModel.getListings().observe(tenantActivity, Observer { listings ->
            for (e in listings) {
                try {
                    var latLng = LatLng(e.latitude.toDouble(), e.longitude.toDouble())
                    var fullAddress = capitalizeEachWord(e.address) + "\n" + capitalizeEachWord(e.city) + ", " + e.state.toUpperCase() + " " + e.postcode
                    var marker = map.addMarker(MarkerOptions().position(latLng).title(fullAddress))
                    marker.tag = e
                } catch (e: Exception) {
                    Log.e(TAG, "Fail to add tag: $e")
                }
            }
        })

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(apolis, 10.0f))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.zIndex += 1.0f
        return false
    }

    override fun onInfoWindowClick(marker: Marker) {
        var listings = marker.tag as ListingsProperty
        tenantActivity.viewModel.setSelectedListings(listings)
        tenantActivity.goToListingsDetail()
    }

    private fun capitalizeEachWord(string: String): String {
        var inputList = string.split(" ")
        var outputString = ""

        for (e in inputList) {
            outputString += e.capitalize() + " "
        }

        return outputString.trim()
    }

//    private fun getPermission() {
//
//        Dexter.withActivity(tenantActivity).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(object :
//            MultiplePermissionsListener {
//            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                if (report.areAllPermissionsGranted()) {
//                    isPermissionsGranted = true
//                }
//
//                if (report.isAnyPermissionPermanentlyDenied) {
//                    showPermissionDeniedMessage()
//                }
//            }
//
//            override fun onPermissionRationaleShouldBeShown(
//                permissions: List<PermissionRequest>,
//                token: PermissionToken
//            ) {
//                token.continuePermissionRequest()
//            }
//        }).onSameThread().check()
//    }

//    private fun openSettings() {
//        val builder = AlertDialog.Builder(MyApplication.context)
//        builder.setTitle("Need Permission")
//        builder.setMessage("We need your permission to access your current location. Please grant the access.")
//        builder.setPositiveButton(
//            "Go to settings"
//        ) { dialog, which ->
//            dialog.dismiss()
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            val uri = Uri.fromParts("package", tenantActivity.packageName, null)
//            intent.data = uri
//            startActivityForResult(intent, 101)
//        }
//        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
//        builder.show()
//    }
//
//    private fun showPermissionDeniedMessage() {
//        val builder = AlertDialog.Builder(MyApplication.context).apply {
//            setTitle("Notification")
//            setMessage("You have denied the location access. We will use our custom location to locate on the map.")
//            setNegativeButton("Dismiss") { dialog, which ->  }
//        }
//        builder.show()
//
//    }
}