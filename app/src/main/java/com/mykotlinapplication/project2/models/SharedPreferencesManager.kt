package com.mykotlinapplication.project2.models

import android.content.Context
import com.mykotlinapplication.project2.MyApplication
import com.mykotlinapplication.project2.views.activities.MainActivity

object SharedPreferencesManager {

    private val sharedPreferences = MyApplication.context.getSharedPreferences("AvantGarde", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setUserLoginInfo(userId: String, userType: String, userEmail: String, appApiKey: String) {
        editor.putString("user.id", userId)
        editor.putString("user.type", userType)
        editor.putString("user.email", userEmail)
        editor.putString("user.appApiKey", appApiKey)
        editor.commit()
    }

    fun setLoginInfo(email: String, password: String, rememberMe: Boolean) {
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("remember", rememberMe)
        editor.commit()
    }

    fun getLoginInfo(): ArrayList<Any> {
        var result = arrayListOf<Any>()
        result.add(sharedPreferences.getString("email", "")!!)
        result.add(sharedPreferences.getString("password", "")!!)
        result.add(sharedPreferences.getBoolean("remember", false))
        return result
    }

}