package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class EquipmentsNew (

    @SerializedName("status"  ) var status  : Int?               = null,
    @SerializedName("message" ) var message : String?            = null,
    @SerializedName("error"   ) var error   : Boolean?           = null,
    @SerializedName("isData"  ) var isData  : Boolean?           = null,
    @SerializedName("results" ) var results_equipments : ArrayList<Results_Equipments> = arrayListOf()

): Serializable

data class Results_Equipments (

    @SerializedName("id"             ) var id            : Int?                     = null,
    @SerializedName("name"           ) var name          : String?                  = null,
    @SerializedName("sub_categories" ) var subCategories_equipments : ArrayList<SubCategories_Equipments> = arrayListOf()

):Serializable



data class SubCategories_Equipments (

    @SerializedName("id"                 ) var id               : Int?               = null,
    @SerializedName("name"               ) var name             : String?            = null,
    @SerializedName("device_category_id" ) var deviceCategoryId : Int?               = null,
    @SerializedName("devices"            ) var devices_euipments          : ArrayList<Devices_Equipments> = arrayListOf()

): Serializable

data class Devices_Equipments (

    @SerializedName("id"                     ) var id                  : Int?              = null,
    @SerializedName("name"                   ) var name                : String?           = null,
    @SerializedName("device_sub_category_id" ) var deviceSubCategoryId : Int?              = null,
    @SerializedName("latest_device_data"     ) var latestDeviceData    : LatestDeviceData_Equipments? = LatestDeviceData_Equipments()

): Serializable
data class LatestDeviceData_Equipments (

    @SerializedName("id"                  ) var id                : Int?               = null,
    @SerializedName("equipment_id"        ) var equipmentId       : Int?               = null,
    @SerializedName("gateway_hardware_id" ) var gatewayHardwareId : String?            = null,
    @SerializedName("updated_at"          ) var createdAt         : String?            = null,
    @SerializedName("details"             ) var details_equipments           : ArrayList<Details_Equipments> = arrayListOf()

): Serializable

data class Details_Equipments (

    @SerializedName("id"                    ) var id                 : Int?    = null,
    @SerializedName("latest_device_data_id" ) var latestDeviceDataId : Int?    = null,
    @SerializedName("parameter_name"        ) var parameterName      : String? = null,
    @SerializedName("unit_name"             ) var unitName           : String? = null,
    @SerializedName("unit_short_name"       ) var unitShortName      : String? = null,
    @SerializedName("data_type"             ) var dataType           : String? = null,
    @SerializedName("datastream"            ) var datastream         : String? = null,
    @SerializedName("datastream_value"      ) var datastreamValue    : String? = null,
    @SerializedName("datastream_value_masking"  ) var datastreamValueMasking    : String? = null
):Serializable






