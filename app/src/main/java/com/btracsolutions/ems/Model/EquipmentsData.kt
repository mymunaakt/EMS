package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName

data class EquipmentsData (

    @SerializedName("success" ) var success : Boolean?           = null,
    @SerializedName("status"  ) var status  : Int?               = null,
    @SerializedName("message" ) var message : String?            = null,
    @SerializedName("results" ) var results : ArrayList<ResultsData> = arrayListOf()
)

data class Details (

    @SerializedName("id"               ) var id              : Int?    = null,
    @SerializedName("device_data_id"   ) var deviceDataId    : Int?    = null,
    @SerializedName("datastream"       ) var datastream      : String? = null,
    @SerializedName("datastream_value" ) var datastreamValue : String? = null,
    @SerializedName("parameter_name"   ) var parameterName   : String? = null,
    @SerializedName("unit_short_name"  ) var unitShortName   : String? = null,
    @SerializedName("unit_name"        ) var unitName        : String? = null

)

data class ResultsData (

    @SerializedName("id"           ) var id          : Int?               = null,
    @SerializedName("equipment_id" ) var equipmentId : Int?               = null,
    @SerializedName("created_at"   ) var createdAt   : String?            = null,
    @SerializedName("details"      ) var details     : ArrayList<Details> = arrayListOf()

)