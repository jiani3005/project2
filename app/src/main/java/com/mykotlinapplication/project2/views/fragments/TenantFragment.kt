package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentTenantBinding
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.views.activities.LandlordActivity
import com.mykotlinapplication.project2.views.adapters.TenantListAdapter

class TenantFragment: Fragment() {

    private lateinit var binding: FragmentTenantBinding
    private lateinit var landlordActivity: LandlordActivity
    private lateinit var tenantListAdapter: TenantListAdapter
    private var tenantList = arrayListOf<Tenant>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tenant, container, false)
        landlordActivity.editAppBar("Tenant", true)
        initTenantListAdapter()

        landlordActivity.viewModel.getTenants().observe(landlordActivity, Observer { tenantArrayList ->
            tenantList = tenantArrayList
            tenantListAdapter.setData(tenantArrayList)
        })


        return binding.root
    }

    private fun initTenantListAdapter() {
        tenantListAdapter = TenantListAdapter(tenantList)
        binding.recyclerViewTenant.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTenant.adapter = tenantListAdapter
    }

}