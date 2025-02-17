package com.btracsolutions.ems.NetworkRepositories

import com.btracsolutions.ems.Model.Equipments
import retrofit2.Response

class EquipRepository {
    suspend fun equipmentList(token: String): Response<Equipments>? {
        return  UserApi.getEquipmentList()?.getAllEquipments(token = token)
    }
}