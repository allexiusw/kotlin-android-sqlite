package com.tinyappco.databasedemo

import java.io.Serializable

class Module (val code: String, var name: String) : Comparable<Module>, Serializable {
    override fun compareTo(other: Module): Int {
        return code.compareTo(other.code)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Module){
            return code.equals(other.code)
        }
        return super.equals(other)
    }

    override fun toString(): String {
        return "$code: $name"
    }
}