package com.tinyappco.databasedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.support.v4.view.KeyEventDispatcher
import java.util.*

class DataManager(val context: Context) {

    val db : SQLiteDatabase

    init {
        db = context.openOrCreateDatabase("Assessment", Context.MODE_PRIVATE, null)
        val componentCreateQuery = "CREATE TABLE IF NOT EXISTS `Components` ( `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Title` TEXT NOT NULL, `Weight` INTEGER NOT NULL, `Deadline` INTEGER, `ModuleCode` TEXT )"
        val moduleCreateQuery = "CREATE TABLE IF NOT EXISTS `Modules` ( `Code` TEXT NOT NULL, `Name` TEXT NOT NULL, PRIMARY KEY(`Code`) )"

        db.execSQL(moduleCreateQuery)
        db.execSQL(componentCreateQuery)
    }
    

    //region create
    fun add(module: Module){

        if (module(module.code) == null) {
            val query = "INSERT INTO modules (code, name) VALUES ('${module.code}', '${module.name}')"
            db.execSQL(query)
        }
    }

    fun add(component: AssessmentComponent){

        val query = "INSERT INTO components (title, weight, deadline, moduleCode) " +
                "VALUES ('${component.title}', '${component.weight}', ${component.deadline.time}, '${component.module.code}')"
        db.execSQL(query)

        //val contentValues = ContentValues()


        //db.insert("components",null,contentValues)
    }
    //endregion


    //region retrieve

    fun allModules() : List<Module>{

        val modules = mutableListOf<Module>()

        val cursor = db.rawQuery("SELECT * FROM modules", null)
        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getString(cursor.getColumnIndex("code"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val module = Module(code,name)
                modules.add(module)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return modules;
    }

    fun module(code: String) : Module? {
        val query = "SELECT * FROM Modules WHERE Code='$code'"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("Code"))
            cursor.close()
            return Module(code,name)
        } else {
            cursor.close()
            return null;
        }
    }

    fun componentsForModule(module: Module) : List<AssessmentComponent>{

        var components = mutableListOf<AssessmentComponent>()

        val query = "SELECT * FROM components WHERE modulecode='" + module.code + "'"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val weight = cursor.getInt(cursor.getColumnIndex("Weight"))
                val dateLong = cursor.getLong(cursor.getColumnIndex("Deadline"))
                val date = Date(dateLong)
                val component = AssessmentComponent(id, title, weight, date, module)
                components.add(component)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return components
    }

    //endregion


    //region update
    fun update(module:Module){
        //db.update()
    }

    fun update(component: AssessmentComponent){

    }
    //endregion


    //region delete
    fun delete(component: AssessmentComponent){

        if (component.id != null) {
            val deletedId = db.delete("components", "id = ${component.id}", null)
        }
        else {
            //todo: find component
        }
    }

    fun delete(module: Module){

        //check for components and delete these first
    }
    //endregion



}