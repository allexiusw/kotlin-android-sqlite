package com.tinyappco.databasedemo

import java.io.Serializable

class Module (val code: String, var name: String) : Comparable<Module>, Serializable {
    override fun compareTo(other: Module): Int {

        return code.compareTo(other.code)

    }

    override fun toString(): String {
        return "$code: $name"
    }



}