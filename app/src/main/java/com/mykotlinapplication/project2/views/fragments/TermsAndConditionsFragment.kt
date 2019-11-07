package com.mykotlinapplication.project2.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.FragmentTermsAndConditionsBinding
import com.mykotlinapplication.project2.views.activities.MainActivity

class TermsAndConditionsFragment: Fragment() {

    private lateinit var binding: FragmentTermsAndConditionsBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_conditions, container, false)

        binding.buttonDone.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
//            mainActivity.goToRegister()
        }

        return binding.root
    }

}