package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentAddPropertyBinding
import com.mykotlinapplication.project2.utilities.AddPropertyListener
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class AddPropertyFragment: Fragment(), AddPropertyListener {

    private lateinit var binding: FragmentAddPropertyBinding
    private lateinit var landlordActivity: LandlordActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landlordActivity = context as LandlordActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_property, container, false)
        landlordActivity.editAppBar("Add Property", true)
        landlordActivity.viewModel.addPropertyListener = this

        landlordActivity.viewModel.getIsUpdating().observe(landlordActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

        })

        binding.buttonAdd.setOnClickListener {
            var address = binding.editTextAddress.text.toString()
            var city = binding.editTextCity.text.toString()
            var state = binding.editTextState.text.toString()
            var country = binding.editTextPostalCode.text.toString()
            var property_status = binding.editTextStatus.text.toString()
            var price = binding.editTextPrice.text.toString()
            var mortgageInfo = binding.editTextMortgageInfo.text.toString()

            landlordActivity.viewModel.addProperty(address, city, state, country, property_status, price, mortgageInfo).observe(landlordActivity,
                Observer { isSuccess ->
                    if (isSuccess) {
                        onSuccess()
//                        landlordActivity.viewModel.addPendingProperty()
                        landlordActivity.supportFragmentManager.popBackStack()
                        landlordActivity.goToProperty()
                    } else {
                        onFailure()
                    }
            })

        }

        return binding.root
    }

    private fun onSuccess() {
        Toast.makeText(landlordActivity, "Successfully added!", Toast.LENGTH_SHORT).show()
    }

    private fun onFailure() {
        Toast.makeText(landlordActivity, "Fail to added property.\nPlease try again.", Toast.LENGTH_SHORT).show()
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

    override fun setPropertyStatusError() {
        binding.editTextStatus.error = "Field must be filled"
        binding.editTextStatus.requestFocus()
    }

    override fun setPriceError() {
        binding.editTextPrice.error = "Field must be filled"
        binding.editTextPrice.requestFocus()
    }

    override fun setMortgageInfoError() {
        binding.editTextMortgageInfo.error = "Field must be filled"
        binding.editTextMortgageInfo.requestFocus()
    }

}