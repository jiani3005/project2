package com.mykotlinapplication.project2.views.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.ActivityMainBinding
import com.mykotlinapplication.project2.helpers.MainHelper
import com.mykotlinapplication.project2.models.databases.Image
import com.mykotlinapplication.project2.models.databases.ImageDatabase
import com.mykotlinapplication.project2.viewmodels.MainViewModel
import com.mykotlinapplication.project2.views.fragments.ForgotPasswordFragment
import com.mykotlinapplication.project2.views.fragments.LoginFragment
import com.mykotlinapplication.project2.views.fragments.RegisterFragment
import com.mykotlinapplication.project2.views.fragments.TermsAndConditionsFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.third_party_user_alert.view.*

class MainActivity : AppCompatActivity(), MainHelper {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val TAG = "MainActivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_GOOGLE_SIGN_IN = 101
    private lateinit var fbCallbackManager: CallbackManager
    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        fbCallbackManager = CallbackManager.Factory.create()

        viewModel.checkLoginSession().observe(this, Observer { isSessionExists ->
//            Log.d(TAG, "isSessionExist = $isSessionExists")
            if (isSessionExists != null) {
                if (isSessionExists == true) {
                    supportFragmentManager.beginTransaction().add(R.id.main_fragment_container, LoginFragment()).commit()
                    goToTenantActivity()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.main_fragment_container, LoginFragment()).commit()
                    goToLandlordActivity()
                }
            } else {
                supportFragmentManager.beginTransaction().add(R.id.main_fragment_container, LoginFragment()).commit()
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

    override fun goToTermsAndConditions() {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, TermsAndConditionsFragment()).addToBackStack(null).commit()
    }

    override fun goToTenantActivity() {

        startActivity(Intent(this, TenantActivity::class.java))
    }

    override fun goToLandlordActivity() {
        startActivity(Intent(this, LandlordActivity::class.java))
    }

    override fun loginWithGoogle() {
        val signInGoogleIntent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun loginWithFacebook() {
        fb_login_button.setReadPermissions("email", "public_profile")
        fb_login_button.registerCallback(fbCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                viewModel.userFacebookLogin(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val acct = task.getResult(ApiException::class.java)
                viewModel.userGoogleLogin(acct!!)
            } catch (e: ApiException) {
                Log.e("FirebaseAuth", "Google sign in failed")
            }
        }
    }

    override fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).setActionTextColor(resources.getColor(R.color.white))
        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
        snackbar.show()

    }

    override fun showAlertDialog() {
        if (::dialog.isInitialized) {
            dialog.dismiss()
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.third_party_user_alert, null)

        val builder = AlertDialog.Builder(this).apply {
            setTitle("Select Type of User")
            setView(dialogView)
        }
        dialog = builder.show()

        dialogView.textView_googleUser_confirm.setOnClickListener {
            if (dialogView.radioGroup_googleUserChoice.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select one of the options", Toast.LENGTH_SHORT).show()
                dialogView.radioGroup_googleUserChoice.requestFocus()
            } else if (dialogView.radioButton_googleTenant.isChecked) {
                Toast.makeText(this@MainActivity, "tenant selected", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                viewModel.registerGoogleUserOnApi("tenant")
            } else if (dialogView.radioButton_googleLandlord.isChecked) {
                Toast.makeText(this@MainActivity, "landlord selected", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                viewModel.registerGoogleUserOnApi("landlord")
            }
        }

        dialogView.textView_googleUser_back.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun setUpNotification() {
        val channel = NotificationChannel("AvantGarde", "AvantGarde", NotificationManager.IMPORTANCE_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        var userInfo  = viewModel.getForgotPasswordInfo()

        val intent = Intent(this, PasswordRecoveryActivity::class.java).apply {
            putExtra("email", userInfo.first)
            putExtra("password", userInfo.second)
        }
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = Notification.Builder(this, channel.id).apply {
            setChannelId(channel.id)
            setContentTitle("Avant Garde")
            setContentText("Click here to retrieve the password.")
            setSmallIcon(R.drawable.notifications)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        notificationManager.notify(1, builder.build())
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
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
        }

    }


}
