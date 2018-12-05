package com.tinyappco.databasedemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_component_details.*
import java.util.*

class ComponentDetailsActivity : AppCompatActivity() {

    lateinit var dataMgr : DataManager
    lateinit var modules : List<Module>

    var existingComponent : AssessmentComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_details)


        dataMgr = DataManager(this)

        refreshSpinner()

        //pre-existing component?

        val component = intent.getSerializableExtra("component")
        if (component is AssessmentComponent){
            existingComponent = component
            etTitle.setText(component.title)
            etWeight.setText(component.weight.toString())

            val cal = Calendar.getInstance()
            cal.time = component.deadline

            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null)

            btnAdd.text = "Update"
        }

    }

    fun refreshSpinner(){
        modules = dataMgr.allModules().sorted();
        val adapter = ArrayAdapter<Module>(this,android.R.layout.simple_spinner_item, modules)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun addModule(v: View){
        val intent = Intent(this,ModuleDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshSpinner()
    }


    fun addComponent(v: View){

        val title = etTitle.text.toString()
        val weight = etWeight.text.toString().toInt()
        val date = datePicker.date()
        val selectedModule = modules[spinner.selectedItemPosition]

        val immutableExistingComponent = existingComponent

        if (immutableExistingComponent != null) {
            immutableExistingComponent.title = title
            immutableExistingComponent.weight = weight
            immutableExistingComponent.deadline = datePicker.date()
            immutableExistingComponent.module = selectedModule

            dataMgr.update(immutableExistingComponent)
        } else {
            val component = existingComponent ?: AssessmentComponent(null, title, weight, date, selectedModule)
            dataMgr.add(component)
        }

        finish()

    }
}
