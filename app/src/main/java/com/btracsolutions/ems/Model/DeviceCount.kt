package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName


data class DeviceCount (

    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("status"  ) var status  : String?  = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("results" ) var results : ResultsDeviceCount? = ResultsDeviceCount()


)

data class ResultsDeviceCount (

    @SerializedName("total"             ) var total            : Int? = null,
    @SerializedName("active"            ) var active           : Int? = null,
    @SerializedName("inactive"          ) var inactive         : Int? = null,
    @SerializedName("active_percentage" ) var activePercentage : Int? = null

)