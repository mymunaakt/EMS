package com.btracsolutions.ems.Sessions

import android.content.Context
import android.content.SharedPreferences
import com.btracsolutions.ems.R

object SessionManager {

    const val USER_TOKEN = "user_token"
    const val USER_NAME = "user_name"
    const val USER_EMAIL = "user_email"
    const val USER_PHONE = "user_phone"
    const val USER_IMAGE = "user_image"
    const val NOTIFICATION_COUNT = "notification_count"

    /**
     * Function to save auth token
     */
    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }

    fun saveUserName(context: Context, userName: String) {
        saveString(context, USER_NAME, userName)
    }

    fun saveUserEmail(context: Context, userEmail: String) {
        saveString(context, USER_EMAIL, userEmail)
    }

    fun saveUserPhone(context: Context, userPhone: String) {
        saveString(context, USER_PHONE, userPhone)
    }

    fun saveUserImage(context: Context, userImage: String) {
        saveString(context, USER_IMAGE, userImage)
    }

    fun saveNotificationCount(context: Context, notificationCount: String) {
        saveString(context, NOTIFICATION_COUNT, notificationCount)
    }


    /**
     * Function to fetch auth token
     */
    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }

    fun getUserName(context: Context): String? {
        return getString(context, USER_NAME)
    }

    fun getUserEmail(context: Context): String? {
        return getString(context, USER_EMAIL)
    }

    fun getUserPhone(context: Context): String? {
        return getString(context, USER_PHONE)
    }

    fun getNotificationCount(context: Context): String? {
        return getString(context, NOTIFICATION_COUNT)
    }


    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}