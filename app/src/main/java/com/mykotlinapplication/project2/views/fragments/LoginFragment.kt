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
import com.mykotlinapplication.project2.utilities.LoginListener
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentLoginBinding
import com.mykotlinapplication.project2.views.activities.MainActivity

class LoginFragment: Fragment(), LoginListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        mainActivity.viewModel.loginListener = this

        mainActivity.viewModel.loadLoginInfo().observe(mainActivity, Observer {
            binding.editTextEmail.setText(it[0].toString())
            binding.editTextPassword.setText(it[1].toString())
            binding.checkBoxRememberMe.isChecked = it[2] as Boolean
        })

        binding.buttonLogin.setOnClickListener {
//            var isTenant = mainActivity.viewModel.userLogin(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.checkBoxRememberMe.isChecked)
            mainActivity.viewModel.userLogin(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.checkBoxRememberMe.isChecked).observe(mainActivity,
                Observer { isTenant ->
                    if (isTenant != null) {
                        if (isTenant) {
                            mainActivity.goToTenantActivity()
                        } else {
                            mainActivity.goToLandlordActivity()
                        }
                    }

            })


        }

        binding.textViewForgotPassword.setOnClickListener {
            mainActivity.goToForgotPassword()
        }

        binding.textViewSignup.setOnClickListener {
            mainActivity.goToRegister()
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