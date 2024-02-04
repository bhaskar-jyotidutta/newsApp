package com.bhaskar.newsapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

class Utils {

    companion object{

        fun convertTo12HourFormat(isoDateString: String): String {
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = isoFormatter.parse(isoDateString)
            val twelveHourFormatter = SimpleDateFormat("hh:mm:ss a dd MMM yyyy", Locale.getDefault())
            return twelveHourFormatter.format(date!!)
        }


    }

}