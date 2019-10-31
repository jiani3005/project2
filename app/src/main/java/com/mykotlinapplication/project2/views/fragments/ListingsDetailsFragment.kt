package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentListingDetailBinding
import com.mykotlinapplication.project2.databinding.FragmentMapBinding
import com.mykotlinapplication.project2.views.activities.TenantActivity

class ListingsDetailsFragment: Fragment() {

    private lateinit var binding: FragmentListingDetailBinding
    private lateinit var tenantActivity: TenantActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tenantActivity = context as TenantActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing_detail, container, false)
        tenantActivity.editAppBar("Property Details", true)

        tenantActivity.viewModel.getSelectedListings().observe(tenantActivity, Observer { selectedListings ->
            binding.textViewAddress.text = "${capitalizeEachWord(selectedListings.address)}\n${capitalizeEachWord(selectedListings.city)}, ${selectedListings.state.toUpperCase()} ${selectedListings.postcode}"
            binding.textViewPrice.text = "$ ${selectedListings.price}"
            binding.textViewStatus.text = "Status: ${selectedListings.propertyStatus.capitalize()}"
            binding.textViewDescription.text = "Mortgage Info: ${selectedListings.mortgageInfo.capitalize()}"

        })

        binding.buttonShare.setOnClickListener {
            tenantActivity.shareProperty()
        }


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