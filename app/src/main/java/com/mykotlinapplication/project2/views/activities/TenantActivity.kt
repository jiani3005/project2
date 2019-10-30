package com.mykotlinapplication.project2.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityTenantBinding
import com.mykotlinapplication.project2.helpers.TenantHelper
import com.mykotlinapplication.project2.viewmodels.TenantViewModel
import com.mykotlinapplication.project2.views.fragments.ListingsDetailsFragment
import com.mykotlinapplication.project2.views.fragments.ListingsFragment
import com.mykotlinapplication.project2.views.fragments.TenantMapFragment
import com.mykotlinapplication.project2.views.fragments.TenantProfileFragment

class TenantActivity : AppCompatActivity(), TenantHelper {

    lateinit var viewModel: TenantViewModel
    private lateinit var binding: ActivityTenantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tenant)

        viewModel = ViewModelProviders.of(this).get(TenantViewModel::class.java)
        goToListings()


        binding.buttonBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.tenantBottomNavigation.setOnNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.action_tenant_listings -> goToListings()
                R.id.action_tenant_map -> goToTenantMap()
                else -> goToProfile()
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

    override fun goToListings() {
        supportFragmentManager.beginTransaction().replace(R.id.tenant_container, ListingsFragment()).addToBackStack(null).commit()
    }

    override fun goToListingsDetail() {
        supportFragmentManager.beginTransaction().replace(R.id.tenant_container, ListingsDetailsFragment()).addToBackStack(null).commit()
    }

    override fun goToTenantMap() {
        supportFragmentManager.beginTransaction().replace(R.id.tenant_container, TenantMapFragment()).addToBackStack(null).commit()
    }

    override fun goToProfile() {
        supportFragmentManager.beginTransaction().replace(R.id.tenant_container, TenantProfileFragment()).addToBackStack(null).commit()
    }

    override fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount == 0) {
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

//        }
//        else {
//            super.onBackPressed()
//        }
    }
}
