package com.clap2esp.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val repository = SettingsRepository(this)

        val ipEdit = findViewById<EditText>(R.id.ipEdit)
        val pathEdit = findViewById<EditText>(R.id.pathEdit)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val settings = repository.load()

        ipEdit.setText(settings.espAddress)
        pathEdit.setText(settings.togglePath)

        saveButton.setOnClickListener {

            val newSettings = SettingsModel(
                espAddress = ipEdit.text.toString(),
                togglePath = pathEdit.text.toString(),
                mode = settings.mode,
                sequence = settings.sequence,
                manualTimeout = settings.manualTimeout,
                timeout = settings.timeout,
                vibration = settings.vibration,
                networkLog = settings.networkLog,
                connectionIndicator = settings.connectionIndicator
            )

            repository.save(newSettings)

            Toast.makeText(
                this,
                "Settings saved",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
