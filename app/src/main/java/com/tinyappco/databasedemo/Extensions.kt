package com.tinyappco.databasedemo

import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

fun DatePicker.date() : Date{
    val cal = Calendar.getInstance()
    cal.set(year,month,dayOfMonth)
    return cal.time
}

fun Date.shortDate() : String{
    val formatter = SimpleDateFormat("dd/MM/yy", Locale.UK)
    return formatter.format(this)
}