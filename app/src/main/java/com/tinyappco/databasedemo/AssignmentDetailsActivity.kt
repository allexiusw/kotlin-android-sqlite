package com.tinyappco.databasedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_assignment_details.*
import java.util.*

class AssignmentDetailsActivity : AppCompatActivity() {

    private lateinit var dataMgr : DataManager
    private lateinit var modules : List<Module>

    private var existingAssignment : Assignment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_details)

        dataMgr = DataManager(this)

        refreshSpinner()

        val assignment = intent.getSerializableExtra("assignment")
        if (assignment is Assignment){
            existingAssignment = assignment
            etTitle.setText(assignment.title)
            etWeight.setText(assignment.weight.toString())

            val cal = Calendar.getInstance()
            cal.time = assignment.deadline
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null)

            //todo: choose module in spinner

            btnAdd.text = getString(R.string.update)
            title = getString(R.string.edit_assignment)
        } else {
            title = getString(R.string.add_assignment)
        }

    }



    override fun onStart() {
        super.onStart()
        btnAdd.isEnabled = dataMgr.hasModules()
    }

    private fun refreshSpinner(){
        modules = dataMgr.allModules()
        val adapter = ArrayAdapter<Module>(this,android.R.layout.simple_spinner_item, modules)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    @Suppress("UNUSED_PARAMETER")
    fun addModule(v: View){
        val intent = Intent(this,ModuleDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshSpinner()
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAssignment(v: View){

        val title = etTitle.text.toString()
        val weight = etWeight.text.toString().toInt()
        val date = datePicker.date()
        val selectedModule = modules[spinner.selectedItemPosition]

        val immutableExistingAssignment = existingAssignment

        if (immutableExistingAssignment != null) {
            immutableExistingAssignment.title = title
            immutableExistingAssignment.weight = weight
            immutableExistingAssignment.deadline = datePicker.date()
            immutableExistingAssignment.module = selectedModule

            dataMgr.update(immutableExistingAssignment)
        } else {
            val assignment = existingAssignment ?: Assignment(null, title, weight, date, selectedModule)
            dataMgr.add(assignment)
        }

        finish()
    }
}
