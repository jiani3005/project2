package com.mykotlinapplication.project2.views.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityMainBinding
import com.mykotlinapplication.project2.helpers.MainHelper
import com.mykotlinapplication.project2.viewmodels.MainViewModel
import com.mykotlinapplication.project2.views.fragments.ForgotPasswordFragment
import com.mykotlinapplication.project2.views.fragments.LoginFragment
import com.mykotlinapplication.project2.views.fragments.RegisterFragment

class MainActivity : AppCompatActivity(), MainHelper {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.checkLoginSession().observe(this, Observer { isSessionExists ->
//            Log.d(TAG, "isSessionExist = $isSessionExists")
            if (isSessionExists != null) {
                if (isSessionExists == true) {
                    goToTenantActivity()
                } else {
                    goToLandlordActivity()
                }
            } else {
                goToLogin()
            }
        })


    }

    override fun goToLogin() {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, LoginFragment()).addToBackStack(null).commit()
    }

    override fun goToRegister() {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, RegisterFragment()).addToBackStack(null).commit()
    }

    override fun goToForgotPassword() {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, ForgotPasswordFragment()).addToBackStack(null).commit()
    }

    override fun goToTenantActivity() {
        startActivity(Intent(this, TenantActivity::class.java))
    }

    override fun goToLandlordActivity() {
        startActivity(Intent(this, LandlordActivity::class.java))
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }

    }
}
