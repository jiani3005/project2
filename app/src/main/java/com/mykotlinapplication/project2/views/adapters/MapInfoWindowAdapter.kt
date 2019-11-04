package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.views.fragments.TenantMapFragment
import kotlinx.android.synthetic.main.property_map_info_content.view.*

class MapInfoWindowAdapter(val context: Fragment): GoogleMap.InfoWindowAdapter {

    private val contents = context.layoutInflater.inflate(R.layout.property_map_info_content, null)

    override fun getInfoContents(marker: Marker): View {
        contents.tv_propertyContent_address.text = marker.title
        var landlordProperty = marker.tag as? LandlordProperty
        var listingsProperty = marker.tag as? ListingsProperty

        if (landlordProperty != null) {
            Glide.with(context).load(landlordProperty.image).into(contents.iv_propertyContent_image)
        } else {
            Glide.with(context).load(listingsProperty?.image).into(contents.iv_propertyContent_image)
        }

        return contents
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}