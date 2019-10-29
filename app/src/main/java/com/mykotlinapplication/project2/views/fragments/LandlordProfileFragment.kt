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
import com.mykotlinapplication.project2.databinding.FragmentProfileBinding
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class LandlordProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var landlordActivity: LandlordActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        landlordActivity.editAppBar("Profile", true)

        landlordActivity.viewModel.getUserEmail().observe(landlordActivity, Observer { userInfo ->
            binding.textViewEmail.text = userInfo.first
            binding.textViewUserType.text = userInfo.second.capitalize()
        })


        binding.buttonLogout.setOnClickListener {
            landlordActivity.viewModel.clearLoginSession()
            landlordActivity.goToMainActivity()
        }

        return binding.root
    }

}