package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentGalleryBinding
import com.mykotlinapplication.project2.databinding.FragmentPropertyBinding
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.views.activities.LandlordActivity
import com.mykotlinapplication.project2.views.adapters.PropertyListAdapter

class PropertyFragment: Fragment() {

    private lateinit var binding: FragmentPropertyBinding
    private lateinit var landlordActivity: LandlordActivity
    private lateinit var propertyListAdapter: PropertyListAdapter
    private var properties = arrayListOf<Property>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_property, container, false)
        landlordActivity.editAppBar("Property", false)

        initPropertyRecyclerView()


        binding.buttonAddProperty.setOnClickListener {
            landlordActivity.goToAddProperty()
        }

        landlordActivity.viewModel.getIsUpdating().observe(landlordActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        landlordActivity.viewModel.getProperty().observe(landlordActivity, Observer {propertyList ->
            if (propertyList != null) {
                properties = propertyList
                propertyListAdapter.setData(propertyList)
            }
        })


        return binding.root
    }

    private fun initPropertyRecyclerView() {
        propertyListAdapter = PropertyListAdapter(properties)
        binding.recyclerViewGallery.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewGallery.adapter = propertyListAdapter
    }


}