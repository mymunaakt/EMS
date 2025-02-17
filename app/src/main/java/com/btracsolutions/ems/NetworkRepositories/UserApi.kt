package com.btracsolutions.ems.NetworkRepositories

import com.btracsolutions.ems.Model.EquipmentDetailsData
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Model.LoginResponse
import com.btracsolutions.ems.Model.Logout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import com.btracsolutions.ems.Model.LoginRequest as LoginRequest1


interface UserApi {

    @POST("auth/oauth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest1): Response<LoginResponse>

    @GET("auth/api/v1/equipment/equipments")
    suspend fun getAllEquipments(@Header("Authorization") token: String) : Response<Equipments>


    @POST("auth/oauth/logout")
    suspend fun logout(@Header("Authorization") token: String) : Response<Logout>


    @POST("auth/api/v1/equipment/stream-data/{id}")
    suspend fun getEquipmentData(@Path("id") id: Int?,@Header("Authorization") token: String,@Query("request_time") request_time:String):Response<EquipmentDetailsData>




    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }

        fun getEquipmentList(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }

        fun getEquipmentDetailsData(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }

        fun userLogOut(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }

    }
}