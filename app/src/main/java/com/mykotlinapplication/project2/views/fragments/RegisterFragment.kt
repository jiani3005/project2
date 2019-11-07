package com.mykotlinapplication.project2.views.fragments

import android.app.AlertDialog
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
import com.mykotlinapplication.project2.utilities.RegisterListener
import com.mykotlinapplication.project2.databinding.FragmentRegisterBinding
import com.mykotlinapplication.project2.views.activities.MainActivity

class RegisterFragment: Fragment(), RegisterListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        mainActivity.viewModel.registerListener = this

        mainActivity.viewModel.getIsUpdating().observe(mainActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

        })

        binding.textViewTerms.setOnClickListener {
            mainActivity.goToTermsAndConditions()
        }

        binding.buttonRegister.setOnClickListener {
            var isTenant = false
            if (binding.radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(mainActivity, "Please select one of the options", Toast.LENGTH_SHORT).show()
                binding.radioGroup.requestFocus()
            } else if (!binding.checkBoxAcceptedTerms.isChecked) {
                showAlertDialog()
            } else {
                if (binding.radioButtonTenant.isChecked) {
                    isTenant = true
                }

                mainActivity.viewModel.userRegister(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.editTextConfirmPassword.text.toString(), isTenant)
                    .observe(mainActivity, Observer { isRegisterSuccess ->
                        if (isRegisterSuccess) {
                            onSuccess()
                            mainActivity.supportFragmentManager.popBackStack()
                            mainActivity.goToLogin()
                        } else {
                            onFailure()
                        }
                })
            }


        }

        return binding.root
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(mainActivity).apply {
            setTitle("ACCEPT TERMS")
            setMessage("Please accept the terms of service in order to proceed.")
            setNegativeButton("Dismiss"){dialog, which ->  }
        }
        builder.create().show()
    }

    override fun setEmailError(message: String) {
        binding.editTextEmail.error = message
        binding.editTextEmail.requestFocus()
    }

    override fun setPasswordError(message: String) {
        binding.editTextPassword.error = message
        binding.editTextPassword.requestFocus()
    }

    override fun setConfirmPasswordError(message: String) {
        binding.editTextConfirmPassword.error = message
        binding.editTextConfirmPassword.requestFocus()
    }

    private fun onFailure() {
//        Toast.makeText(mainActivity, "Register failed.\nEmail already exists.", Toast.LENGTH_SHORT).show()
        mainActivity.showSnackbar("Register failed.\nEmail already exists.")
    }

    private fun onSuccess() {
//        Toast.makeText(mainActivity, "Successfully registered!", Toast.LENGTH_SHORT).show()
        mainActivity.showSnackbar("Successfully registered!")
    }

}