package com.btracsolutions.flowwithapi.Api

import com.btracsolutions.ems.Model.*

import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    fun getUsers(): Flow<List<Equipments>>
    fun getMoreUsers(): Flow<List<Equipments>>
    fun getUsersWithError(): Flow<List<Equipments>>


    fun login(loginRequest: LoginRequest): Flow<LoginResponse>

    fun totalDeviceCount(token: String): Flow<DeviceCount>

    fun logout(token: String): Flow<Logout>

    fun getAllEquipments(token: String): Flow<Equip>
    //new equipment api
    fun getEquipments(token: String): Flow<EquipmentsNew>


    //push notification
    fun getPushNotification(token: String, hardware_token:String): Flow<PushNotification>

    fun transferRelayCommand(token: String,command: String, relay: String, hardware_id: String ): Flow< RelayCommand>
    fun getallEquipmentData(id:Int, token: String, request_time: String, order_by: String, order: String) : Flow<EquipmentsData>


}