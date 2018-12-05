package com.tinyappco.databasedemo

import java.io.Serializable
import java.util.*

class AssessmentComponent(var id: Int?, var title:String, var weight: Int, var deadline: Date, var module:Module) : Comparable<AssessmentComponent>, Serializable {


    override fun compareTo(other: AssessmentComponent): Int {
        return deadline.compareTo(other.deadline)
    }

    fun daysLeft() : Int{
        val difference = (deadline.time - Date().time) / (1000 * 60 * 60 * 24)
        return difference.toInt()
    }


    override fun toString(): String {
        return "${deadline.shortDate()} ${module.code}: $title ($weight%)"
    }





}