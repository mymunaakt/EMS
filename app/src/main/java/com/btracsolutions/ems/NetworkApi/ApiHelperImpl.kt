package com.btracsolutions.flowwithapi.Api

import com.btracsolutions.ems.Model.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override fun getUsers() = flow {
        emit(apiService.getUsers())
    }
    override fun getMoreUsers() = flow {
        emit(apiService.getMoreUsers())
    }
    override fun getUsersWithError() = flow {
        emit(apiService.getUsersWithError())
    }

    override fun login(loginRequest: LoginRequest)= flow {

        emit(apiService.loginUser(loginRequest = loginRequest ))



    }

    override fun totalDeviceCount(token: String)= flow {

        emit(apiService.deviceCount(token = token))



    }

    override fun logout(token: String)= flow {

        emit(apiService.logout(token = token))



    }

    //push notification
    override fun getPushNotification(token: String,hardware_token: String)= flow {

        emit(apiService.sendToken(token = token,hardware_token = hardware_token))



    }


    override fun getAllEquipments(token:String)= flow {

           emit(apiService.getAllEquipments(token = token ))



    }

    override fun getEquipments(token: String)= flow {
        emit(apiService.getEquipments(token = token ))
    }

    override fun transferRelayCommand(
        token: String,
        command: String,
        relay: String,
        hardware_id: String
    )= flow {
        emit(apiService.sendRelayCommand(token = token,command = command,relay =relay,hardware_id=hardware_id))
    }

    override fun getallEquipmentData(
        id: Int,
        token: String,
        request_time: String,
        order_by: String,
        order: String
    )= flow {
        emit(apiService.allEquipmentData(id = id,token = token,request_time =request_time, order_by = order_by, order = order))
    }


}