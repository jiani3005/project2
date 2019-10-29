package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        landlordActivity.viewModel.getSelectedProperty().observe(landlordActivity, Observer { selectedProperty ->
            binding.textViewAddress.text = "${capitalizeEachWord(selectedProperty.address)}\n${capitalizeEachWord(selectedProperty.city)}, ${selectedProperty.state.toUpperCase()} ${selectedProperty.country}"
            binding.textViewPrice.text = "$ ${selectedProperty.price}"
            binding.textViewStatus.text = "Status: ${selectedProperty.status.capitalize()}"
            binding.textViewDescription.text = "Mortgage Info: ${selectedProperty.mortgageInfo.capitalize()}"

            binding.textViewAddTenant.setOnClickListener {
//                Toast.makeText(landlordActivity, "Property ID = ${selectedProperty.id}", Toast.LENGTH_SHORT).show()
                landlordActivity.goToAddTenant()
            }
        })


//        Log.d(TAG, property.toString())



        return binding.root
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