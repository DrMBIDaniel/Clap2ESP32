package com.clap2esp.app.settings

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clap2esp.app.R

class SettingsActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val ipEdit=findViewById<EditText>(R.id.ipEdit)
        val pathEdit=findViewById<EditText>(R.id.pathEdit)
        val saveButton=findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener{
            Toast.makeText(this,"Settings will be saved in next step",Toast.LENGTH_SHORT).show()
        }
    }
}
