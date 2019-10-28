package com.mykotlinapplication.project2.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityLandlordBinding
import com.mykotlinapplication.project2.helpers.LandlordHelper
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.viewmodels.LandlordViewModel
import com.mykotlinapplication.project2.views.fragments.*

class LandlordActivity : AppCompatActivity(), LandlordHelper {

    private lateinit var binding: ActivityLandlordBinding
    lateinit var viewModel: LandlordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landlord)
        viewModel = ViewModelProviders.of(this).get(LandlordViewModel::class.java)

        goToProperty()

        binding.buttonBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.landlordBottomNavigation.setOnNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.action_landlord_property -> goToProperty()
                R.id.action_landlord_tenant -> goToTenant()
                R.id.action_landlord_map -> goToMap()
                R.id.action_landlord_profile -> goToProfile()
            }

            true
        }

    }

    override fun editAppBar(title: String, showBackButton: Boolean) {
        binding.textViewTitle.text = title
        if (showBackButton) {
            binding.buttonBack.visibility = View.VISIBLE
        } else {
            binding.buttonBack.visibility = View.INVISIBLE
        }
    }

    override fun goToProperty() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, PropertyFragment()).addToBackStack(null).commit()
    }

    override fun goToPropertyDetails(property: Property) {
//        val bundle = Bundle().apply {
//            putParcelable("property", property)
//        }
//        val propertyDetailsFragment = PropertyDetailsFragment().apply { arguments = bundle }
        viewModel.setSelectedProperty(property)
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, PropertyDetailsFragment()).addToBackStack(null).commit()
    }

    override fun goToAddProperty() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, AddPropertyFragment()).addToBackStack(null).commit()
    }

    override fun goToTenant() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, TenantFragment()).addToBackStack(null).commit()
    }

    override fun goToTenantDetails(tenant: Tenant) {
//        val bundle = Bundle().apply {
//            putParcelable("tenant", tenant)
//        }
//        val tenantDetailsFragment = TenantDetailsFragment().apply { arguments = bundle }
        viewModel.setSelectedTenant(tenant)
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, TenantDetailsFragment()).addToBackStack(null).commit()
    }

    override fun goToAddTenant(property: Property) {
//        val bundle = Bundle().apply {
//            putParcelable("property", property)
//        }
//        val addTenantFragment = AddTenantFragment().apply { arguments = bundle }
        viewModel.setSelectedProperty(property)
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, AddTenantFragment()).addToBackStack(null).commit()
    }

    override fun goToMap() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, LandlordMapFragment()).addToBackStack(null).commit()
    }

    override fun goToProfile() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, LandlordProfileFragment()).addToBackStack(null).commit()
    }

    override fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}
