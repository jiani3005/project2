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
import com.mykotlinapplication.project2.databinding.FragmentTenantDetailBinding
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class TenantDetailsFragment: Fragment() {

    private lateinit var binding: FragmentTenantDetailBinding
    private lateinit var landlordActivity: LandlordActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tenant_detail, container, false)
        landlordActivity.editAppBar("Tenant Details", true)

//        val tenant= arguments?.getParcelable<Tenant>("tenant")!!

        landlordActivity.viewModel.getSelectedTenant().observe(landlordActivity, Observer { selectedTenant ->
            binding.textViewName.text = capitalizeEachWord(selectedTenant.name)
            binding.textViewPhone.text = selectedTenant.phone
            binding.textViewEmail.text = selectedTenant.email
            binding.textViewAddress.text = capitalizeEachWord(selectedTenant.address)
        })

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