package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName


data class RelayCommand (

    @SerializedName("status"  ) var status  : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("isError" ) var isError : Boolean? = null,
    @SerializedName("data"    ) var data    : String?  = null,
    @SerializedName("results" ) var resultsRelay : ResultsRelay? = ResultsRelay()

)

data class ResultsRelay (

    @SerializedName("status"  ) var status  : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("isError" ) var isError : Boolean? = null,
    @SerializedName("data"    ) var data    : String?  = null

)