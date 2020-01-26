package com.tinyappco.databasedemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.*

class DataManager(context: Context) {

    private val db: SQLiteDatabase = context.openOrCreateDatabase("Assessment", Context.MODE_PRIVATE, null)

    init {
        val modulesCreateQuery =
            "CREATE TABLE IF NOT EXISTS `Modules` ( `Code` TEXT NOT NULL, `Name` TEXT NOT NULL, PRIMARY KEY(`Code`) )"
        val assignmentsCreateQuery =
            "CREATE TABLE IF NOT EXISTS `Assignments` ( `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Title` TEXT NOT NULL, `Weight` INTEGER NOT NULL, `Deadline` INTEGER, `ModuleCode` TEXT )"

        db.execSQL(modulesCreateQuery)
        db.execSQL(assignmentsCreateQuery)
    }


    //region create
    fun add(module: Module): Boolean {

        return if (module(module.code) == null) {
            val query = "INSERT INTO Modules (code, name) VALUES ('${module.code}', '${module.name}')"
            db.execSQL(query)
            true
        } else {
            false
        }
    }

    fun add(assignment: Assignment) {

        val query = "INSERT INTO Assignments (Title, Weight, Deadline, ModuleCode) " +
                "VALUES ('${assignment.title}', '${assignment.weight}', ${assignment.deadline.time}, '${assignment.module.code}')"
        db.execSQL(query)
    }
    //endregion


    //region retrieve

    fun hasModules(): Boolean {
        val cursor = db.rawQuery("SELECT * FROM Modules", null)
        return if (cursor.count > 0) {
            cursor.close()
            true
        } else {
            cursor.close()
            false
        }
    }


    fun allModules(): List<Module> {

        val modules = mutableListOf<Module>()

        val cursor = db.rawQuery("SELECT * FROM Modules", null)
        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getString(cursor.getColumnIndex("Code"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val module = Module(code, name)
                modules.add(module)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return modules.sorted()
    }

    private fun module(code: String): Module? {
        val query = "SELECT * FROM Modules WHERE Code='$code'"
        val cursor = db.rawQuery(query, null)

        return if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("Code"))
            cursor.close()
            Module(code, name)
        } else {
            cursor.close()
            null
        }
    }

    private fun assignments(query: String, args: Array<String>?): List<Assignment> {
        val assignments = mutableListOf<Assignment>()

        val cursor = db.rawQuery(query, args)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val modCode = cursor.getString(cursor.getColumnIndex("ModuleCode"))
                val weight = cursor.getInt(cursor.getColumnIndex("Weight"))
                val dateLong = cursor.getLong(cursor.getColumnIndex("Deadline"))
                val date = Date(dateLong)
                val assignment = Assignment(id, title, weight, date, module(modCode)!!)
                assignments.add(assignment)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return assignments.sorted()
    }


    private fun assignmentsForModule(module: Module): List<Assignment> {
        val query = "SELECT * FROM Assignments WHERE ModuleCode = ?"
        return assignments(query, arrayOf(module.code))
    }

    fun assignments(): List<Assignment> {
        val query = "SELECT * FROM Assignments"
        return assignments(query, null)
    }

    //endregion


    //region update
    fun update(module:Module){
        val contentValues = ContentValues()
        contentValues.put("Name", module.name)
        val args = arrayOf(module.code)
        db.update("Modules",contentValues,"Code = ?", args)
    }


    //equivalent of above
//    fun update(module: Module) {
//        val query = """UPDATE Modules SET
//            Name = '${module.name}'
//            WHERE Code = '${module.code}'
//        """
//        db.execSQL(query)
//    }




    fun update(assignment: Assignment) {
        val contentValues = ContentValues()
        contentValues.put("Title", assignment.title)
        contentValues.put("Weight", assignment.weight)
        contentValues.put("Deadline", assignment.deadline.time)
        contentValues.put("ModuleCode", assignment.module.code)
        val args = arrayOf(assignment.id.toString())
        db.update("Assignments",contentValues,"Id = ?",args)
    }



//    fun update(assignment: Assignment) {
//
//        val query = """UPDATE Assignments SET
//                Title = '${assignment.title}',
//                Weight = ${assignment.weight},
//                Deadline = ${assignment.deadline.time},
//                ModuleCode = '${assignment.module.code}'
//                WHERE Id = ${assignment.id}
//                """
//        db.execSQL(query)
//    }
    //endregion


    //region delete
    fun delete(assignment: Assignment) {

        if (assignment.id != null) {
            val whereClause = "Id = ?"
            val whereArgs = arrayOf(assignment.id.toString())
            db.delete("Assignments", whereClause, whereArgs)
        }
    }

    fun delete(module: Module) {

        //check for assignments and delete these first
        val moduleAssignments = assignmentsForModule(module)
        for (assignment in moduleAssignments) {
            delete(assignment)
        }
        db.delete("Modules", "Code = ?", arrayOf(module.code))
    }
    //endregion

}