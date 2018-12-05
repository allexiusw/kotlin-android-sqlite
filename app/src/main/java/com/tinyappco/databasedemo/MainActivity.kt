package com.tinyappco.databasedemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataManager = DataManager(this)

        val prog1 = Module("CO4225", "Programming I")

        dataManager.add(prog1)

        val module = dataManager.module("CO4225")

        if (module != null){
            Log.wtf("MAIN_ACTIVITY", module.name)
        }
         else{
            Log.wtf("MAIN_ACTIVITY", "Module is null")
        }




    }







}
