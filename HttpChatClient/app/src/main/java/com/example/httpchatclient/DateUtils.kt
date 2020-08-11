package com.example.httpchatclient

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateUtils {
    companion object ConvertDate {
        fun getDateText(date: Date): String {
            if (TimeUnit.HOURS.convert(Date().time - date.time, TimeUnit.MILLISECONDS) >= 24) {
                return SimpleDateFormat("dd/MM/yyyy").format(date)
            }
            return SimpleDateFormat("h:mm a").format(date)
        }
    }
}