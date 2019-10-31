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
import com.bumptech.glide.Glide
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentTenantDetailBinding
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class TenantDetailsFragment: Fragment() {

    private val TAG = "TenantDetailsFragment"
    private lateinit var binding: FragmentTenantDetailBinding
    private lateinit var landlordActivity: LandlordActivity
    private var currentTenant = Tenant("", "", "", "", "", "")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tenant_detail, container, false)
        landlordActivity.editAppBar("Tenant Details", true)
        initCallFunction()
        initMessageFunction()
        initEmailFunction()

        landlordActivity.viewModel.getSelectedTenant().observe(landlordActivity, Observer { selectedTenant ->
            currentTenant = selectedTenant
            binding.textViewName.text = capitalizeEachWord(selectedTenant.name)
            binding.textViewPhone.text = selectedTenant.phone
            binding.textViewEmail.text = selectedTenant.email
            binding.textViewAddress.text = capitalizeEachWord(selectedTenant.address)
            Glide.with(landlordActivity).load(selectedTenant.image).into(binding.imageViewProfile)
        })

        return binding.root
    }

    private fun initCallFunction() {
        binding.callIcon.setOnClickListener {
            landlordActivity.performCommunicationAction("Call Tenant", "Call ${capitalizeEachWord(currentTenant.name)} at ${currentTenant.phone}?", "call")
        }

        binding.textViewPhone.setOnClickListener {
            landlordActivity.performCommunicationAction("Call Tenant", "Call ${capitalizeEachWord(currentTenant.name)} at ${currentTenant.phone}?", "call")
        }
    }

    private fun initMessageFunction() {
        binding.messageIcon.setOnClickListener {
            landlordActivity.performCommunicationAction("Message Tenant", "Message ${capitalizeEachWord(currentTenant.name)} at ${currentTenant.phone}?", "message")
        }
    }

    private fun initEmailFunction() {
        binding.emailIcon.setOnClickListener {
            landlordActivity.performCommunicationAction("Email Tenant", "Email ${capitalizeEachWord(currentTenant.name)} at ${currentTenant.email}?", "email")
        }

        binding.textViewEmail.setOnClickListener {
            landlordActivity.performCommunicationAction("Email Tenant", "Email ${capitalizeEachWord(currentTenant.name)} at ${currentTenant.email}?", "email")
        }
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