package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentAddTenantBinding
import com.mykotlinapplication.project2.utilities.AddTenantListener
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class AddTenantFragment: Fragment(), AddTenantListener {

    private lateinit var binding: FragmentAddTenantBinding
    private lateinit var landlordActivity: LandlordActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_tenant, container, false)
        landlordActivity.editAppBar("Add Tenant", true)
        landlordActivity.viewModel.addTenantListener = this

        landlordActivity.viewModel.getSelectedProperty().observe(landlordActivity, Observer { selectedProperty ->
            binding.buttonAdd.setOnClickListener {
                var name = binding.editTextName.text.toString()
                var phone = binding.editTextPhone.text.toString()
                var email = binding.editTextEmail.text.toString()
                var address = binding.editTextAddress.text.toString()
                var city = binding.editTextCity.text.toString()
                var state = binding.editTextState.text.toString()
                var postcode = binding.editTextPostalCode.text.toString()

                landlordActivity.viewModel.addTenant(name, phone, email, address, city, state, postcode, selectedProperty.id).observe(landlordActivity,
                    Observer { isSuccess ->
                        if (isSuccess) {
//                            landlordActivity.viewModel.addPendingTenant()
                            onSuccess()
//
                            landlordActivity.supportFragmentManager.popBackStack()
                            landlordActivity.goToPropertyDetails()
                        } else {
                            onFailure()
                        }
                })
            }
        })


        landlordActivity.viewModel.getIsUpdating().observe(landlordActivity, Observer {isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun setNameError() {
        binding.editTextName.error = "Field must be filled"
        binding.editTextName.requestFocus()
    }

    override fun setPhoneError(message: String) {
        binding.editTextPhone.error = message
        binding.editTextPhone.requestFocus()
    }

    override fun setEmailError(message: String) {
        binding.editTextEmail.error = message
        binding.editTextEmail.requestFocus()
    }

    override fun setAddressError() {
        binding.editTextAddress.error = "Field must be filled"
        binding.editTextAddress.requestFocus()
    }

    override fun setCityError() {
        binding.editTextCity.error = "Field must be filled"
        binding.editTextCity.requestFocus()
    }

    override fun setStateError() {
        binding.editTextState.error = "Field must be filled"
        binding.editTextState.requestFocus()
    }

    override fun setPostcodeError(message: String) {
        binding.editTextPostalCode.error = message
        binding.editTextPostalCode.requestFocus()
    }

    private fun onFailure() {
        Toast.makeText(landlordActivity, "Fail to add.\nPlease try a different email.", Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() {
        Toast.makeText(landlordActivity, "Successfully added!", Toast.LENGTH_SHORT).show()
    }

}