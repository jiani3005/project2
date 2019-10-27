package com.mykotlinapplication.project2.models

import com.google.gson.JsonElement
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("pro_mgt_login.php")
    fun userLogin(@Query("email") email: String, @Query("password") password: String): Call<JsonElement>

    @GET("pro_mgt_reg.php")
    fun userRegister(@Query("email") email: String, @Query("landlord_email") landlordEmail: String,
                     @Query("password") password: String, @Query("account_for") accountFor: String): Call<ResponseBody>

    @GET("pro_mgt_forgot_pass.php")
    fun userForgotPassword(@Query("email") email: String): Call<JsonElement>

}