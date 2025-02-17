package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName

data class Time (
    @SerializedName("TimeinHr")
    var time_in_hr: String,
    @SerializedName("TimeInMinute")
    var time_in_minute: String,
    var isSelected:Boolean=false

        )