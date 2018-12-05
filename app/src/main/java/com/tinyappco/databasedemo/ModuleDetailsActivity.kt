package com.tinyappco.databasedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_module_details.*



class ModuleDetailsActivity : AppCompatActivity() {

    var existingModule : Module? = null
    lateinit var dataManager : DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_details)

        dataManager = DataManager(this)

        var module = intent.getSerializableExtra("module")
        if (module is Module){

            existingModule = module
            etTitle.setText(module.name)
            etCode.setText(module.code)
            etCode.isEnabled = false //cant change code as primary key - delete module instead
            button.text = "Update"
        }



    }

    fun addModule(v: View){

        var immutableExistingModule = existingModule
        if (immutableExistingModule != null){
            immutableExistingModule.name = etTitle.text.toString()
            dataManager.update(immutableExistingModule)
            finish()
        } else {


            val code = etCode.text.toString();
            if (validateModuleCode(code)) {
                val module = Module(etCode.text.toString(), etTitle.text.toString())
                val result = dataManager.add(module)

                if (result) {

                    finish()
                } else {
                    tvError.text = "Unable to add module, does the module already exist?"
                }


            } else {
                tvError.text = "Module code should be two upper case letters followed by four numbers"
            }

        }
    }


    fun validateModuleCode(code: String) : Boolean{

        val regEx = "([A-Z]{2})([4-7])([0-9]{3})"
        return code.matches(Regex(regEx))
    }

}
