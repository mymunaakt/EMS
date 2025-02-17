package com.btracsolutions.ems.Utils

import com.btracsolutions.ems.Model.Devices
import com.btracsolutions.ems.Model.Devices_Equipments
import com.btracsolutions.ems.Model.SubCategories
import com.btracsolutions.ems.Model.SubCategories_Equipments

object Constant {
   // const val BASE_URL = "http://13.251.135.12:8081"
  //  const val BASE_URL = "https://ems.btracsolutions.com/backend/"
    const val BASE_URL = "https://ems.btracsolutions.com/backend/"
    val equipmentForSearch = ArrayList<Devices>()
    val equipmentSearch = ArrayList<Devices_Equipments>()

    val deviceSubCategoryList = ArrayList<SubCategories>()
    val deviceSubCategoryEquipmentList = ArrayList<SubCategories_Equipments>()



}