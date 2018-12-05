package com.tinyappco.databasedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_module_details.*

class ModuleDetailsActivity : AppCompatActivity() {

    private var existingModule : Module? = null
    private lateinit var dataManager : DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_details)

        dataManager = DataManager(this)

        val module = intent.getSerializableExtra("module")
        if (module is Module){
            existingModule = module
            etTitle.setText(module.name)
            etCode.setText(module.code)
            etCode.isEnabled = false //cant change code as primary key - delete module instead
            button.text = getString(R.string.update)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addModule(v: View){

        val immutableExistingModule = existingModule
        if (immutableExistingModule != null){
            immutableExistingModule.name = etTitle.text.toString()
            dataManager.update(immutableExistingModule)
            finish()
        } else {

            val code = etCode.text.toString()
            if (validateModuleCode(code)) {
                val module = Module(etCode.text.toString(), etTitle.text.toString())
                val result = dataManager.add(module)

                if (result) {

                    finish()
                } else {
                    tvError.text = getString(R.string.module_exists)
                }

            } else {
                tvError.text = getString(R.string.invalid_module_code)
            }
        }
    }

    private fun validateModuleCode(code: String) : Boolean{
        val regEx = "([A-Z]{2})([4-7])([0-9]{3})"
        return code.matches(Regex(regEx))
    }

}
