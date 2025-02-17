package com.btracsolutions.ems.NetworkRepositories

import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Utils.Constant
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitService {


    @GET("/auth/api/v1/equipment/equipments")
    fun getAllEquipments(@Header("Authorization") token: String) : Call<Equipments>

    companion object {
        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
