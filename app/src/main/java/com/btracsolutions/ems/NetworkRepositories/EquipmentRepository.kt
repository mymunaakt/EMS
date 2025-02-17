package com.btracsolutions.ems.NetworkRepositories

class EquipmentRepository constructor(private val retrofitService: RetrofitService) {
    fun getAllEquipments(token:String) = retrofitService.getAllEquipments(token = token)
}