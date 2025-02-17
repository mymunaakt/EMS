package com.btracsolutions.ems.Utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException as ParseException1

class DateConvertion {

    @SuppressLint("SimpleDateFormat")
    fun formatDate(inputDateString: String, outputFormat: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        try {
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(inputDateString)

            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
            outputDateFormat.timeZone = TimeZone.getDefault()

            return outputDateFormat.format(date)
        } catch (e: ParseException1) {
            Log.e("Error", e.toString())
            return "Error formatting date"
        }
    }




}