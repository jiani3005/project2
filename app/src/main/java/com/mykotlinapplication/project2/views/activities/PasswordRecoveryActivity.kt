package com.mykotlinapplication.project2.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityRetrievePasswordBinding
import com.mykotlinapplication.project2.viewmodels.MainViewModel

class PasswordRecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetrievePasswordBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "PasswordRecoveryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_retrieve_password)

        binding.textViewEmail.text = "Email: " + intent.getStringExtra("email")
        binding.textViewPassword.text = "Password: " + intent.getStringExtra("password")
    }
}
