package com.mykotlinapplication.project2.models.databases

import com.google.gson.JsonElement
import com.mykotlinapplication.project2.models.ListingsPropertyList
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("pro_mgt_login.php")
    fun userLogin(@Query("email") email: String, @Query("password") password: String): Call<JsonElement>

    @GET("pro_mgt_reg.php")
    fun userRegister(@Query("email") email: String, @Query("landlord_email") landlordEmail: String,
                     @Query("password") password: String, @Query("account_for") accountFor: String): Call<ResponseBody>

    @GET("pro_mgt_forgot_pass.php")
    fun userForgotPassword(@Query("email") email: String): Single<JsonElement>

    @GET("property.php")
    fun getLandlordProperty(@Query("userid") userId: String, @Query("usertype") userType: String): Call<JsonElement>

    @GET("pro_mgt_add_pro.php")
    fun addProperty(@Query("address") address: String, @Query("city") city: String, @Query("state") state: String,
                    @Query("country") country: String, @Query("pro_status") property_status: String,
                    @Query("purchase_price") price: String, @Query("mortage_info") mortgageInfo: String,
                    @Query("userid") userId: String, @Query("usertype") userType: String,
                    @Query("latitude") latitude: String, @Query("longitude") longitude: String): Call<JsonElement>

    @GET("pro_mgt_tenent_details.php")
    fun getTenants(@Query("landlordid") userId: String): Call<JsonElement>

    @GET("pro_mgt_add_tenants.php")
    fun addTenant(@Query("name") name: String, @Query("email") email: String, @Query("address") address: String,
                  @Query("mobile") phone: String, @Query("propertyid") propertyId: String,
                  @Query("landlordid") landlordId: String): Call<ResponseBody>

    @GET("remove-property.php")
    fun deleteProperty(@Query("propertyid") propertyId: String): Call<JsonElement>

    @GET("pro_mgt_property_all.php")
    fun getListings(): Single<ListingsPropertyList>

}