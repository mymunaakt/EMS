package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Equip (

    @SerializedName("status"  ) var status  : Int?               = null,
    @SerializedName("message" ) var message : String?            = null,
    @SerializedName("error"   ) var error   : Boolean?           = null,
    @SerializedName("isData"  ) var isData  : Boolean?           = null,
    @SerializedName("results" ) var results : ArrayList<EquipResults> = arrayListOf()

) : Serializable

data class EquipResults (

    @SerializedName("id"             ) var id            : Int?                     = null,
    @SerializedName("name"           ) var name          : String?                  = null,
    @SerializedName("sub_categories" ) var subCategories : ArrayList<SubCategories> = arrayListOf()

): Serializable

data class SubCategories (

    @SerializedName("id"      ) var id      : Int?               = null,
    @SerializedName("name"    ) var name    : String?            = null,
    @SerializedName("devices" ) var devices : ArrayList<Devices> = arrayListOf()

): Serializable

data class Devices (

    @SerializedName("id"                 ) var id               : Int?              = null,
    @SerializedName("name"               ) var name             : String?           = null,
    @SerializedName("created_at"         ) var createdAt        : String?           = null,
    @SerializedName("hardware_id"        ) var hardwareId       : String?           = null,
    @SerializedName("latest_stream_data" ) var latestStreamData : ArrayList<EquipLatestStreamData> = arrayListOf()

): Serializable

data class EquipLatestStreamData(
    @SerializedName("parameter_short_name" ) var parameterShortName : String? = null,
    @SerializedName("parameter_value"      ) var parameterValue     : String? = null,
    @SerializedName("param_name"           ) var paramName          : String? = null,
    @SerializedName("param_id"             ) var paramId            : String? = null,
    @SerializedName("data_type"            ) var dataType           : String? = null,
    @SerializedName("unit_name"            ) var unitName           : String? = null,
    @SerializedName("unit_short_name"      ) var unitShortName      : String? = null
):Serializable






