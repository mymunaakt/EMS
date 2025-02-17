package com.btracsolutions.ems.Model

import com.google.gson.annotations.SerializedName


data class NotificationList(
    val meta: Meta_Notification,
    val results: List<Notification>
)

data class Meta_Notification(val totalCount: Int, val pageCount: Int, val currentPage: Int, val perPage: Int)
data class Notification(val id: Int, val title: String, val description: String, val created_at: String)