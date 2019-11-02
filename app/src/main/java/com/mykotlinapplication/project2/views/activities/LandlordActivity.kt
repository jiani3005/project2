package com.mykotlinapplication.project2.views.activities

import android.content.Intent
import android.net.Uri
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
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.viewmodels.LandlordViewModel
import com.mykotlinapplication.project2.views.fragments.*

class LandlordActivity : AppCompatActivity(), LandlordHelper {

    private lateinit var binding: ActivityLandlordBinding
    lateinit var viewModel: LandlordViewModel
    private var TAG = "LandlordActivity"
    private var selectedProperty = LandlordProperty()
    private var selectedTenant = Tenant("", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landlord)
        viewModel = ViewModelProviders.of(this).get(LandlordViewModel::class.java)

        viewModel.getSelectedProperty().observe(this, Observer {
            selectedProperty = it
        })

        viewModel.getSelectedTenant().observe(this, Observer {
            selectedTenant = it
        })

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
            setTitle("Delete Property")
            setMessage("Are you sure you want to delete this property?")
            setPositiveButton("Yes") {dialog, which ->
                viewModel.deleteProperty().observe(this@LandlordActivity, Observer { isSuccess ->
                    if (isSuccess) {
//                        viewModel.updatePropertyList()
                        Toast.makeText(this@LandlordActivity, "Property is deleted!", Toast.LENGTH_SHORT).show()
                        goToProperty()
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

    private fun goToCall() {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${selectedTenant.phone}")))

    }

    private fun goToMessage() {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${selectedTenant.phone}")))
    }

    private fun goToEmail() {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${selectedTenant.email}")))
    }

    override fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun shareProperty() {
        val fullAddress = "${capitalizeEachWord(selectedProperty.address)}\n${capitalizeEachWord(selectedProperty.city)}, ${selectedProperty.state.toUpperCase()} ${selectedProperty.country}"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Share Property")
            putExtra(Intent.EXTRA_TEXT, "Check out this property!\nAddress: $fullAddress")
        }
        startActivity(Intent.createChooser(intent, "Share using:"))

    }

    override fun performCommunicationAction(title: String, message: String, action: String) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Yes") {dialog, which ->
                when (action) {
                    "call" -> goToCall()
                    "message" -> goToMessage()
                    "email" -> goToEmail()
                }
            }
            setNegativeButton("No") {dialog, which ->  }
        }
        builder.create().show()
    }

    override fun onBackPressed() {
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
