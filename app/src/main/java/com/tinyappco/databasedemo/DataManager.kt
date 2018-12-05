package com.tinyappco.databasedemo

import android.content.Context
import java.util.*

class DataManager(context: Context) {

    private val db = context.openOrCreateDatabase("Assessment", Context.MODE_PRIVATE, null)

    init {
        val moduleCreateQuery = "CREATE TABLE IF NOT EXISTS `Modules` ( `Code` TEXT NOT NULL, `Name` TEXT NOT NULL, PRIMARY KEY(`Code`) )"
        val componentCreateQuery = "CREATE TABLE IF NOT EXISTS `Components` ( `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Title` TEXT NOT NULL, `Weight` INTEGER NOT NULL, `Deadline` INTEGER, `ModuleCode` TEXT )"

        db.execSQL(moduleCreateQuery)
        db.execSQL(componentCreateQuery)
    }


    //region create
    fun add(module: Module) : Boolean{

        return if (module(module.code) == null) {
            val query = "INSERT INTO modules (code, name) VALUES ('${module.code}', '${module.name}')"
            db.execSQL(query)
            true
        } else {
            false
        }
    }

    fun add(component: AssessmentComponent) {

        val query = "INSERT INTO Components (Title, Weight, Deadline, ModuleCode) " +
                "VALUES ('${component.title}', '${component.weight}', ${component.deadline.time}, '${component.module.code}')"
        db.execSQL(query)
    }
    //endregion


    //region retrieve

    fun allModules() : List<Module>{

        val modules = mutableListOf<Module>()

        val cursor = db.rawQuery("SELECT * FROM Modules", null)
        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getString(cursor.getColumnIndex("Code"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val module = Module(code,name)
                modules.add(module)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return modules.sorted()
    }

    private fun module(code: String) : Module? {
        val query = "SELECT * FROM Modules WHERE Code='$code'"
        val cursor = db.rawQuery(query,null)

        return if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("Code"))
            cursor.close()
            Module(code,name)
        } else {
            cursor.close()
            null
        }
    }

    private fun components(query: String) : List<AssessmentComponent>{
        val components = mutableListOf<AssessmentComponent>()

        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val modCode =  cursor.getString(cursor.getColumnIndex("ModuleCode"))
                val weight = cursor.getInt(cursor.getColumnIndex("Weight"))
                val dateLong = cursor.getLong(cursor.getColumnIndex("Deadline"))
                val date = Date(dateLong)
                val component = AssessmentComponent(id, title, weight, date, module(modCode)!!)
                components.add(component)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return components.sorted()
    }


    private fun componentsForModule(module: Module) : List<AssessmentComponent>{
        val query = "SELECT * FROM Components WHERE ModuleCode='" + module.code + "'"
        return components(query)
    }

    fun components() : List<AssessmentComponent>{
        val query = "SELECT * FROM Components"
        return components(query)
    }

    //endregion


    //region update
    fun update(module:Module){

        val query = """UPDATE Modules SET
            Name = '${module.name}'
            WHERE Code = '${module.code}'
        """
        db.execSQL(query)
    }

    fun update(component: AssessmentComponent){

        val query = """UPDATE Components SET
                Title = '${component.title}',
                Weight = ${component.weight},
                Deadline = ${component.deadline.time},
                ModuleCode = '${component.module.code}'
                WHERE Id = ${component.id}
                """
        db.execSQL(query)
    }
    //endregion


    //region delete
    fun delete(component: AssessmentComponent){

        if (component.id != null) {
            db.delete("Components", "Id = ${component.id}", null)
        }
    }

    fun delete(module: Module){

        //check for components and delete these first
        val moduleComponents = componentsForModule(module)
        for (component in moduleComponents){
            delete(component)
        }

        db.delete("Modules","Code = '${module.code}'", null)

    }
    //endregion

}