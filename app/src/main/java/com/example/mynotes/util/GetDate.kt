package com.example.mynotes.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(timeStamp: Long): String {
    val string = StringBuilder("Дата создания:\n")

    val instant = Instant.ofEpochMilli(timeStamp)
    val zoneId = ZoneId.systemDefault()
    val localDatetime = LocalDateTime.ofInstant(instant, zoneId)
    val currentDate = LocalDateTime.now()

    val formatter: DateTimeFormatter = if (localDatetime.toLocalDate() == currentDate.toLocalDate()) {
        DateTimeFormatter.ofPattern("сегодня HH:mm")
    } else {
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    }

    string.append(localDatetime.format(formatter))
    return string.toString()
}