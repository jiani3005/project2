package com.mykotlinapplication.project2.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityLandlordBinding
import com.mykotlinapplication.project2.helpers.LandlordHelper
import com.mykotlinapplication.project2.viewmodels.LandlordViewModel
import com.mykotlinapplication.project2.views.fragments.*

class LandlordActivity : AppCompatActivity(), LandlordHelper {

    private lateinit var binding: ActivityLandlordBinding
    lateinit var viewModel: LandlordViewModel
    private var TAG = "LandlordActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landlord)
        viewModel = ViewModelProviders.of(this).get(LandlordViewModel::class.java)

        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, PropertyFragment()).commit()

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

    override fun goToPropertyDetails() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, PropertyDetailsFragment()).addToBackStack(null).commit()
    }

    override fun goToAddProperty() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, AddPropertyFragment()).addToBackStack(null).commit()
    }

    override fun deleteProperty() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Delete LandlordProperty")
            setMessage("Are you sure you want to this property?")
            setPositiveButton("Yes") {dialog, which ->
                viewModel.deleteProperty().observe(this@LandlordActivity, Observer { isSuccess ->
                    if (isSuccess) {
                        viewModel.updatePropertyList()
                        Toast.makeText(this@LandlordActivity, "LandlordProperty is deleted!", Toast.LENGTH_SHORT).show()
//                        viewModel.deleteSuccessProperty()
                    } else {
                        Toast.makeText(this@LandlordActivity, "Fail to delete property. Please try again!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            setNegativeButton("No") { dialog, which ->  
                
            }
        }
        builder.create().show()
    }

    override fun goToTenant() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, TenantFragment()).addToBackStack(null).commit()
    }

    override fun goToTenantDetails() {
        supportFragmentManager.beginTransaction().replace(R.id.landlord_container, TenantDetailsFragment()).addToBackStack(null).commit()
    }

    override fun goToAddTenant() {
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            val builder = AlertDialog.Builder(this).apply {
                setTitle("Exit Application")
                setMessage("Do you want to exit current application?")
                setPositiveButton("Yes") {dialog, which ->
                    finishAffinity()
                }
                setNegativeButton("No") {dialog, which ->

                }
            }
            builder.create().show()

        } else {
            super.onBackPressed()
        }
    }

}
