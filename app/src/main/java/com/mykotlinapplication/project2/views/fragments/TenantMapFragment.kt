package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentMapBinding
import com.mykotlinapplication.project2.databinding.FragmentProfileBinding
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.views.activities.TenantActivity
import com.mykotlinapplication.project2.views.adapters.MapInfoWindowAdapter
import kotlinx.android.synthetic.main.property_map_info_content.view.*
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

        map.moveCamera(CameraUpdateFactory.newLatLng(apolis))
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
}