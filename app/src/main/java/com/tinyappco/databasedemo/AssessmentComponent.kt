package com.tinyappco.databasedemo

import java.util.*

class AssessmentComponent(var id: Int?, var title:String, var weight: Int, var deadline: Date, var module:Module) {

    fun daysLeft() : Int{
        val difference = (deadline.time - Date().time) / (1000 * 60 * 60 * 24)
        return difference.toInt()
    }


}