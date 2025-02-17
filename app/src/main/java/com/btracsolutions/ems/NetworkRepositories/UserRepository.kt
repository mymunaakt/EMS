package com.btracsolutions.ems.NetworkRepositories

import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Model.LoginRequest
import com.btracsolutions.ems.Model.LoginResponse
import com.btracsolutions.ems.Model.Logout
import retrofit2.Call
import retrofit2.Response

class UserRepository {

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
        return  UserApi.getApi()?.loginUser(loginRequest = loginRequest)
    }

    suspend fun logoutUser(token: String): Response<Logout>? {
        return  UserApi.userLogOut()?.logout(token = token)
    }





}