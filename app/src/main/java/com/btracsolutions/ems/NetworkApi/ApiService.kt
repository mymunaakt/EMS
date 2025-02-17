package com.btracsolutions.flowwithapi.Api

import com.btracsolutions.ems.Model.*

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<Equipments>
    @GET("more-users")
    suspend fun getMoreUsers(): List<Equipments>
    @GET("error")
    suspend fun getUsersWithError(): List<Equipments>



    @GET("auth/api/v1/equipment/device_status_count")
    suspend fun deviceCount(@Header("Authorization") token: String): DeviceCount

    @POST("auth/api/v1/equipment/stream-data/{id}")
    suspend fun allEquipmentData(@Path("id") id: Int?,@Header("Authorization") token: String,@Query("request_time") request_time:String,@Query("order_by") order_by:String,@Query("order") order:String):EquipmentsData



    @POST("auth/oauth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/oauth/logout")
    suspend fun logout(@Header("Authorization") token: String) : Logout


    @GET("auth/api/v1/equipment/equipments")
    suspend fun getAllEquipments(@Header("Authorization") token: String) : Equip

    //new equipment api
    @GET("auth/api/v1/equipment/equipments")
    suspend fun getEquipments(@Header("Authorization") token: String) : EquipmentsNew

    //ntification list
    @GET("auth/notification")
    suspend fun getNotifications(
        @Query("\$orderby") orderby: String = "id desc",
        @Query("\$top") top: Int = 10,
        @Query("\$skip") skip: Int,
        @Header("Authorization") token: String): NotificationList



    //push notification
    @POST("auth/notification/user_device_token")
    suspend fun sendToken(@Header("Authorization") token: String,@Query("device_token") hardware_token:String) : PushNotification


    @POST("auth/device/command")
    suspend fun sendRelayCommand(@Header("Authorization") token: String,@Query("command") command:String, @Query("relay") relay:String, @Query("hardware_id") hardware_id:String ) : RelayCommand
}