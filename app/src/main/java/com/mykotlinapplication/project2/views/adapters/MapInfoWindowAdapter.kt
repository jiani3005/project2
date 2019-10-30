package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.views.fragments.TenantMapFragment
import kotlinx.android.synthetic.main.property_map_info_content.view.*

class MapInfoWindowAdapter(val context: Fragment): GoogleMap.InfoWindowAdapter {

    private val contents = context.layoutInflater.inflate(R.layout.property_map_info_content, null)

    override fun getInfoContents(marker: Marker): View {
        contents.tv_propertyContent_address.text = marker.title
        return contents
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}