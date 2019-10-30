package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentGalleryBinding
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.views.activities.TenantActivity
import com.mykotlinapplication.project2.views.adapters.ListingsListAdapter
import com.mykotlinapplication.project2.views.adapters.PropertyListAdapter

class ListingsFragment: Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var tenantActivity: TenantActivity
    private var listings = arrayListOf<ListingsProperty>()
    private lateinit var listingsAdapter: ListingsListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tenantActivity = context as TenantActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        tenantActivity.editAppBar("Property", false)
        initListingsRecyclerView()

        tenantActivity.viewModel.getIsUpdating().observe(tenantActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        tenantActivity.viewModel.getListings().observe(tenantActivity, Observer { list ->
            listings = list
            listingsAdapter.setData(listings)
        })

        return binding.root
    }

    private fun initListingsRecyclerView() {
        listingsAdapter = ListingsListAdapter(listings)
        binding.recyclerViewGallery.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewGallery.adapter = listingsAdapter
    }
}