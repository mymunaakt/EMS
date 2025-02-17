package com.btracsolutions.ems.Utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TimeAgo {
    val timestamp = "2024-09-17 10:40:50"
    val timeAgo = getTimeAgo(parseTimestamp(timestamp))

   // println("Time ago: $timeAgo")
}

fun getTimeAgoNew(timestamp: String): String {
    // Define the pattern of your input date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Parse the input timestamp to LocalDateTime
    val pastDateTime = LocalDateTime.parse(timestamp, formatter)

    // Get the current time
    val now = LocalDateTime.now()

    // Calculate the duration between the two times
    val duration = Duration.between(pastDateTime, now)

    // Get time ago in different units
    val seconds = duration.seconds
    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        seconds < 60 -> "$seconds seconds ago"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> {
            // For dates older than 7 days, return the date
            pastDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}

/*fun main() {
    val timestamp = "2024-09-16 11:36:13"
    val result = getTimeAgo(timestamp)
    println(result) // Output will vary depending on the current time
}*/

fun parseTimestamp(timestamp: String): Instant {
    return Instant.parse(timestamp)
}

fun getTimeAgo(timestamp: Instant): String {
    val currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
    val targetDateTime = timestamp.atOffset(ZoneOffset.UTC).toLocalDateTime()

    val years = ChronoUnit.YEARS.between(targetDateTime, currentDateTime)
    val months = ChronoUnit.MONTHS.between(targetDateTime, currentDateTime)
    val days = ChronoUnit.DAYS.between(targetDateTime, currentDateTime)
    val hours = ChronoUnit.HOURS.between(targetDateTime, currentDateTime)
    val minutes = ChronoUnit.MINUTES.between(targetDateTime, currentDateTime)

    return when {
        years > 0 -> "$years years ago"
        months > 0 -> "$months months ago"
        days > 0 -> "$days days ago"
        hours > 0 -> "$hours hours ago"
        minutes > 0 -> "$minutes minutes ago"
        else -> "Just now"
    }


    }