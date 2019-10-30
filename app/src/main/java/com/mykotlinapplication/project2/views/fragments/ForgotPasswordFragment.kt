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
import com.mykotlinapplication.project2.databinding.FragmentForgotPasswordBinding
import com.mykotlinapplication.project2.utilities.ForgotPasswordListener
import com.mykotlinapplication.project2.views.activities.MainActivity

class ForgotPasswordFragment: Fragment(), ForgotPasswordListener {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        mainActivity.viewModel.forgotPasswordListener = this

        mainActivity.viewModel.getIsUpdating().observe(mainActivity, Observer { isUpdating ->
            if (isUpdating) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

        })

        binding.buttonSubmit.setOnClickListener {
            mainActivity.viewModel.userForgotPassword(binding.editTextEmail.text.toString()).observe(mainActivity,
                Observer { isSuccess ->
                    if (isSuccess) {
                        mainActivity.setUpNotification()
                        onSuccess()
                        mainActivity.supportFragmentManager.popBackStack()
                        mainActivity.goToLogin()
                    } else {
                        onFailure()
                    }
            })
        }

        return binding.root
    }

    override fun setEmailError(message: String) {
        binding.editTextEmail.error = message
        binding.editTextEmail.requestFocus()
    }

    private fun onFailure() {
        Toast.makeText(mainActivity, "Email is not registered.", Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() {
        Toast.makeText(mainActivity, "Please check the notification to retrieve your password.", Toast.LENGTH_SHORT).show()
    }

}