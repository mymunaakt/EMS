package com.btracsolutions.ems.NetworkRepositories

import com.btracsolutions.ems.Model.EquipmentDetailsData
import com.btracsolutions.ems.Model.Equipments
import retrofit2.Response

class EquipmentDetailsDataRepository {
    suspend fun equipmentData(id:Int,token: String, request_time:String): Response<EquipmentDetailsData>? {
        return  UserApi.getEquipmentDetailsData()?.getEquipmentData(id=id,token = token,request_time = request_time)
    }
}