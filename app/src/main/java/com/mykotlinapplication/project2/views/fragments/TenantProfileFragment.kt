package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentProfileBinding
import com.mykotlinapplication.project2.views.activities.TenantActivity

class TenantProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var tenantActivity: TenantActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tenantActivity = context as TenantActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        tenantActivity.editAppBar("Profile", true)


        binding.buttonLogout.setOnClickListener {
            tenantActivity.viewModel.clearLoginSession()
            tenantActivity.goToMainActivity()
        }

        return binding.root
    }
}