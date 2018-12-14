package com.tinyappco.databasedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private lateinit var dataSet : List<Assignment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataManager = DataManager(this)

        refreshDeadlines()

        registerForContextMenu(listView)

        title = getString(R.string.deadlines)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_edit_modules){
            val intent = Intent(this,ModuleListActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_deadlines_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.menu_edit){
            val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val componentToEdit = dataSet[info.position]

            val intent = Intent(this,AssignmentDetailsActivity::class.java)
            intent.putExtra("assignment",componentToEdit)
            startActivity(intent)

            return true
        }

        if (item?.itemId == R.id.menu_delete){

            val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val component = dataSet[info.position]
            dataManager.delete(component)
            refreshDeadlines()

            return true
        }

        return super.onContextItemSelected(item)
    }

    private fun refreshDeadlines(){
        dataSet = dataManager.assignments()
        listView.adapter = ArrayAdapter<Assignment>(this,android.R.layout.simple_list_item_1,dataSet)
    }

    override fun onResume() {
        super.onResume()
        refreshDeadlines()
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAssignment(v: View){
        val intent = Intent(this, AssignmentDetailsActivity::class.java)
        startActivity(intent)
    }







}
