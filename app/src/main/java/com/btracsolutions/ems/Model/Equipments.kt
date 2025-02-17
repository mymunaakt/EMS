package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Equipments (

    @SerializedName("meta"    ) var meta    : Meta?              = Meta(),
    @SerializedName("results" ) var results : ArrayList<Results> = arrayListOf()

        ):Serializable


data class Debug (

    @SerializedName("userId"          ) var userId          : Int?              = null,
    @SerializedName("organizationId"  ) var organizationId  : String?           = null,
    @SerializedName("organizationIds" ) var organizationIds : ArrayList<String> = arrayListOf(),
    @SerializedName("organogramId"    ) var organogramId    : String?           = null,
    @SerializedName("organogramIds"   ) var organogramIds   : ArrayList<String> = arrayListOf(),
    @SerializedName("queryString"     ) var queryString     : String?           = null,
    @SerializedName("userToken"       ) var userToken       : String?           = null

):Serializable

data class Meta (

    @SerializedName("totalCount"  ) var totalCount  : Int?              = null,
    @SerializedName("pageCount"   ) var pageCount   : Int?              = null,
    @SerializedName("currentPage" ) var currentPage : Int?              = null,
    @SerializedName("perPage"     ) var perPage     : Int?              = null,
    @SerializedName("summary"     ) var summary     : ArrayList<String> = arrayListOf(),
    @SerializedName("debug"       ) var debug       : Debug?            = Debug()

):Serializable


data class Equipment (

    @SerializedName("id"                 ) var id               : Int?              = null,
    @SerializedName("name"               ) var name             : String?           = null,
    @SerializedName("created_at"         ) var createdAt        : String?           = null,
    @SerializedName("latest_stream_data" ) var latestStreamData : ArrayList<LatestStreamData> = arrayListOf()

):Serializable

data class LatestStreamData(
    /* "param_name": "Temperature",
                                    "unit_name": "Celcius",
                                    "unit_short_name": "°C",
                                    "param_data": 13.88*/

    @SerializedName("param_name") var param_name               : String?                 = null,
    @SerializedName("unit_name") var unit_name               : String?                 = null,
    @SerializedName("unit_short_name") var unit_short_name               : String?                 = null,
    @SerializedName("param_data"                 ) var param_data              : String?                 = null,
    @SerializedName("param_id"        ) var paramId       : String? = null,
    @SerializedName("data_type"      )  var dataType     : String? = null,
    @SerializedName("parameter_value") var parameter_value : String? = null








):Serializable

data class EquipmentSubCategoryList (

    @SerializedName("id"                 ) var id               : Int?                 = null,
    @SerializedName("organogram_id"      ) var organogramId     : Int?                 = null,
    @SerializedName("organization_id"    ) var organizationId   : Int?                 = null,
    @SerializedName("device_category_id" ) var deviceCategoryId : Int?                 = null,
    @SerializedName("name"               ) var name             : String?              = null,
    @SerializedName("organization_name"  ) var organizationName : String?              = null,
    @SerializedName("organogram_name_en" ) var organogramNameEn : String?              = null,
    @SerializedName("organogram_name_bn" ) var organogramNameBn : String?              = null,
    @SerializedName("category_name"      ) var categoryName     : String?              = null,
    @SerializedName("equipment_qty"      ) var equipmentQty     : Int?                 = null,
    @SerializedName("equipment"          ) var equipment        : ArrayList<Equipment> = arrayListOf()

) : Serializable

data class Results (

    @SerializedName("id"                          ) var id                       : Int?                                = null,
    @SerializedName("uuid"                        ) var uuid                     : String?                             = null,
    @SerializedName("organogram_id"               ) var organogramId             : Int?                                = null,
    @SerializedName("organization_id"             ) var organizationId           : Int?                                = null,
    @SerializedName("name"                        ) var name                     : String?                             = null,
    @SerializedName("description"                 ) var description              : String?                             = null,
    @SerializedName("icon"                        ) var icon                     : String?                             = null,
    @SerializedName("created_by"                  ) var createdBy                : Int?                                = null,
    @SerializedName("updated_by"                  ) var updatedBy                : Int?                                = null,
    @SerializedName("created_at"                  ) var createdAt                : String?                             = null,
    @SerializedName("updated_at"                  ) var updatedAt                : String?                             = null,
    @SerializedName("sort_order"                  ) var sortOrder                : Int?                                = null,
    @SerializedName("status"                      ) var status                   : Int?                                = null,
    @SerializedName("organization_name"           ) var organizationName         : String?                             = null,
    @SerializedName("organogram_name_en"          ) var organogramNameEn         : String?                             = null,
    @SerializedName("organogram_name_bn"          ) var organogramNameBn         : String?                             = null,
    @SerializedName("created_by_name"             ) var createdByName            : String?                             = null,
    @SerializedName("updated_by_name"             ) var updatedByName            : String?                             = null,
    @SerializedName("equipment_sub_category_qty"  ) var equipmentSubCategoryQty  : Int?                                = null,
    @SerializedName("equipment_sub_category_list" ) var equipmentSubCategoryList : ArrayList<EquipmentSubCategoryList> = arrayListOf()

):Serializable