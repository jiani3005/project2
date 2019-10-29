package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.mykotlinapplication.project2.databinding.PropertyMapInfoWindowBinding
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.views.activities.LandlordActivity
import kotlinx.android.synthetic.main.property_map_info_content.*
import kotlinx.android.synthetic.main.property_map_info_content.view.*
import kotlinx.android.synthetic.main.property_map_info_window.view.*

class LandlordMapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var landlordActivity: LandlordActivity
    private lateinit var map: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        landlordActivity.editAppBar("Map", true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        binding.progressBarLayout.visibility = View.VISIBLE
        map = googleMap
        map.setInfoWindowAdapter(PropertyMapInfoWindowAdapter())

        val apolis = LatLng(41.917300, -88.264400)
//        map.addMarker(MarkerOptions().position(apolis).title("Apolis"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(apolis))
        landlordActivity.viewModel.getLocationsCoordinates().observe(landlordActivity, Observer { latLngList ->
            var latLng = LatLng(0.0, 0.0)

            for (e in latLngList) {
                latLng = e.third
                var marker = map.addMarker(MarkerOptions().position(latLng).title(e.second))
                marker.tag = Pair(e.first, e.second)
                marker.showInfoWindow()
            }
        })
        map.moveCamera(CameraUpdateFactory.newLatLng(apolis))
        binding.progressBarLayout.visibility = View.GONE
    }

    inner class PropertyMapInfoWindowAdapter: GoogleMap.InfoWindowAdapter {

        private val window: View = layoutInflater.inflate(R.layout.property_map_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.property_map_info_content, null)

        override fun getInfoContents(marker: Marker): View? {
            contents.tv_propertyContent_address.text = marker.title
            contents.tv_propertyContent_moreInfo.setOnClickListener {
                var item = marker.tag as Pair<Property, String>
                landlordActivity.viewModel.setSelectedProperty(item.first)
                landlordActivity.goToPropertyDetails()
            }
            return contents
        }

        override fun getInfoWindow(marker: Marker): View? {
            window.tv_propertyInfo_address.text = marker.title

            return null
        }

    }

}