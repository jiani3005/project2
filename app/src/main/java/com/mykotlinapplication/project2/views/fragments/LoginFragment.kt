package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private val TAG = "LoginFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.viewModel.loginListener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        mainActivity.viewModel.loadLoginInfo().observe(mainActivity, Observer {
            binding.editTextEmail.setText(it[0].toString())
            binding.editTextPassword.setText(it[1].toString())
            binding.checkBoxRememberMe.isChecked = it[2] as Boolean
        })

        mainActivity.viewModel.getIsUpdating().observe(mainActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

        })

        mainActivity.viewModel.getIsGoogleSignInSuccess().observe(mainActivity, Observer { response ->
            var isSuccess = response["isSuccess"]
            var msg = response["msg"]
            Log.d(TAG, response.toString())

            if (isSuccess == "true") {
                onSuccess()
                if (msg == "tenant") {
                    mainActivity.goToTenantActivity()
                } else {
                    mainActivity.goToLandlordActivity()
                }
                mainActivity.viewModel.clearGoogleSignInResult()
            } else {
                if (msg == "New User") {
                    //show dialog box to choose tenant or landlord
                    mainActivity.showAlertDialog()
                } else if (msg != ""){
                    onFailure(msg!!)
                }
            }
        })

        binding.buttonLogin.setOnClickListener {
//            var isTenant = mainActivity.viewModel.userLogin(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.checkBoxRememberMe.isChecked)
            mainActivity.viewModel.userLogin(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString(), binding.checkBoxRememberMe.isChecked).observe(mainActivity,
                Observer { response ->
                    var isSuccess = response["isSuccess"]
                    var msg = response["msg"]

                    Log.d(TAG, "app user routine")

                    if (isSuccess == "true") {
                        onSuccess()
                        if (msg == "tenant") {
                            mainActivity.goToTenantActivity()
                        } else {
                            mainActivity.goToLandlordActivity()
                        }
                    } else {
                        onFailure(msg!!)
                    }

            })

        }

        binding.textViewForgotPassword.setOnClickListener {
            mainActivity.goToForgotPassword()
        }

        binding.textViewSignup.setOnClickListener {
            mainActivity.goToRegister()
        }

        binding.googleLayout.setOnClickListener {
            mainActivity.loginWithGoogle()
        }

        binding.facebookLayout.setOnClickListener {
            binding.fbLoginButton.performClick()
            Log.d(TAG, "facebook clicked!")
            mainActivity.loginWithFacebook()
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

    private fun onFailure(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() {
        Toast.makeText(mainActivity, "Login Success!", Toast.LENGTH_SHORT).show()
    }

}