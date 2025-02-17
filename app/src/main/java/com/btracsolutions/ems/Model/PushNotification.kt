package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName

data class PushNotification (
    @SerializedName("results" ) var results : ResultsForPush? = ResultsForPush()
)


data class ResultsForPush (

    @SerializedName("code"         ) var code        : Int?    = null,
    @SerializedName("message"      ) var message     : String? = null,
    @SerializedName("device_token" ) var deviceToken : String? = null

)