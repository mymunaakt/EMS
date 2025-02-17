package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("data"    ) var data    : Data?    = Data(),
    @SerializedName("results" ) var results : LogoutResults? = LogoutResults())
data class Data (

    @SerializedName("access_token"  ) var accessToken  : String? = null,
    @SerializedName("refresh_token" ) var refreshToken : String? = null

)
data class User (

    @SerializedName("email" ) var email : String? = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("phone" ) var phone : String? = null

)
data class LogoutResults (

    @SerializedName("access_token"  ) var accessToken  : String? = null,
    @SerializedName("refresh_token" ) var refreshToken : String? = null,
    @SerializedName("user"          ) var user         : User?   = User()

)

