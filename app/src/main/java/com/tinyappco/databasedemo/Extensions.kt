package com.tinyappco.databasedemo

import android.widget.DatePicker
import java.util.*

fun DatePicker.date() : Date{
    val cal = Calendar.getInstance()
    cal.set(year,month,dayOfMonth)
    return cal.time
}