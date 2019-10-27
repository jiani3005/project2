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

        binding.buttonRegister.setOnClickListener {
            var isTenant = false
            if (binding.radioGroup.checkedRadioButtonId == -1) {
                onFailure("Please select one of the options")
                binding.radioGroup.requestFocus()
            } else {
                if (binding.radioButtonTenant.isChecked) {
                    isTenant = true
                }

                mainActivity.viewModel.userRegister(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.editTextConfirmPassword.text.toString(), isTenant)
                    .observe(mainActivity, Observer { isRegisterSuccess ->
                        if (isRegisterSuccess) {
                            mainActivity.supportFragmentManager.popBackStack()
                            mainActivity.goToLogin()
                        }
                })
            }


        }

        return binding.root
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

    override fun onFailure(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

}