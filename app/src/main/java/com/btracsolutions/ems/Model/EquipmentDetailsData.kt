package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName


data class EquipmentDetailsData (

    @SerializedName("success" ) var success : Boolean?           = null,
    @SerializedName("status"  ) var status  : Int?               = null,
    @SerializedName("message" ) var message : String?            = null,
    @SerializedName("results" ) var results : ArrayList<ResultsEquipmentData> = arrayListOf()

)

data class ResultsEquipmentData (

    @SerializedName("equipment_id"   ) var equipmentId   : Int?                     = null,
    @SerializedName("created_at"     ) var createdAt     : String?                  = null,
    @SerializedName("parameter_data" ) var parameterData : ArrayList<ParameterData> = arrayListOf()

)

data class ParameterData (
    @SerializedName("parameter_short_name" ) var parameterShortName : String? = null,
    @SerializedName("parameter_value"      ) var parameterValue     : String? = null,
    @SerializedName("param_name"           ) var paramName          : String? = null,
    @SerializedName("param_id"             ) var paramId            : String? = null,
    @SerializedName("data_type"            ) var dataType           : String? = null,
    @SerializedName("unit_name"            ) var unitName           : String? = null,
    @SerializedName("unit_short_name"      ) var unitShortName      : String? = null




)