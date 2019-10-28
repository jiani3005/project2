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
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentListingDetailBinding
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class PropertyDetailsFragment: Fragment() {

    private lateinit var binding: FragmentListingDetailBinding
    private lateinit var landlordActivity: LandlordActivity
    private val TAG = "PropertyDetailsFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing_detail, container, false)
        binding.textViewAddTenant.visibility = View.VISIBLE
        landlordActivity.editAppBar("Property Details", true)

//        val property: Property = arguments?.getParcelable("property")!!
        val property: Property

        landlordActivity.viewModel.getSelectedProperty().observe(landlordActivity, Observer { selectedProperty ->
            binding.textViewAddress.text = "${selectedProperty.address}\n${selectedProperty.city}, ${selectedProperty.state} ${selectedProperty.country}"
            binding.textViewPrice.text = "$ ${selectedProperty.price}"
            binding.textViewPropertyID.text = "Property ID: #${selectedProperty.id}"
            binding.textViewStatus.text = "Status: ${selectedProperty.status}"
            binding.textViewDescription.text = "Mortgage Info: ${selectedProperty.mortgageInfo}"

            binding.textViewAddTenant.setOnClickListener {
                landlordActivity.goToAddTenant(selectedProperty)
            }
        })


//        Log.d(TAG, property.toString())



        return binding.root
    }

}