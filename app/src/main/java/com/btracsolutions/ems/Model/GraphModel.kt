package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName

data class GraphModel (
    @SerializedName("param_data")
    var graph_param_data: String,
    @SerializedName("created_at")
    var graph_created_at : String


        )