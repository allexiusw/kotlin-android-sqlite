package com.tinyappco.databasedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_module_list.*

class ModuleListActivity : AppCompatActivity() {

    private lateinit var modules : List<Module>
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_list)

        dataManager = DataManager(this)

        refreshList()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val module = modules[position]
            val intent = Intent(this,ModuleDetailsActivity::class.java)
            intent.putExtra("module",module)
            startActivity(intent)
        }

        registerForContextMenu(listView)

        title=getString(R.string.modules)
    }

    private fun refreshList(){
        modules = dataManager.allModules()
        listView.adapter = ArrayAdapter<Module>(this,android.R.layout.simple_list_item_1,modules)
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_modules_context,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_module_delete){

            val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val module = modules[info.position]
            dataManager.delete(module)
            refreshList()
            return true
        }

        return super.onContextItemSelected(item)
    }



    override fun onResume() {
        super.onResume()
        refreshList()
    }
}
