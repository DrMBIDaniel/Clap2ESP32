package com.clap2esp.app.settings

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.clap2esp.app.R
import com.clap2esp.app.network.ConnectionTester

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val repository = SettingsRepository(this)

        val ipEdit = findViewById<EditText>(R.id.ipEdit)
        val pathEdit = findViewById<EditText>(R.id.pathEdit)

        val toggleMode = findViewById<RadioButton>(R.id.toggleMode)
        val onOffMode = findViewById<RadioButton>(R.id.onOffMode)

        val doubleSingle = findViewById<RadioButton>(R.id.doubleSingle)
        val singleDouble = findViewById<RadioButton>(R.id.singleDouble)

        val manualTimeout = findViewById<CheckBox>(R.id.manualTimeout)
        val timeoutEdit = findViewById<EditText>(R.id.timeoutEdit)

        val vibrationCheck = findViewById<CheckBox>(R.id.vibrationCheck)
        val systemLogCheck = findViewById<CheckBox>(R.id.systemLogCheck)
        val audioLogCheck = findViewById<CheckBox>(R.id.audioLogCheck)
        val networkLogCheck = findViewById<CheckBox>(R.id.networkLogCheck)
        val debugLogCheck = findViewById<CheckBox>(R.id.debugLogCheck)
        val connectionCheck = findViewById<CheckBox>(R.id.connectionCheck)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val testButton = findViewById<Button>(R.id.testButton)

        val settings = repository.load()

        ipEdit.setText(settings.espAddress)
        pathEdit.setText(settings.togglePath)

        toggleMode.isChecked = settings.mode == 0
        onOffMode.isChecked = settings.mode == 1

        doubleSingle.isChecked = settings.sequence == 0
        singleDouble.isChecked = settings.sequence == 1

        manualTimeout.isChecked = settings.manualTimeout
        timeoutEdit.setText(settings.timeout.toString())

        vibrationCheck.isChecked = settings.vibration
        systemLogCheck.isChecked = settings.showSystemLog
        audioLogCheck.isChecked = settings.showAudioLog
        networkLogCheck.isChecked = settings.showNetworkLog
        debugLogCheck.isChecked = settings.showDebugLog
        connectionCheck.isChecked = settings.connectionIndicator

        saveButton.setOnClickListener {

            var address = ipEdit.text.toString().trim()

            address = address
                .removePrefix("http://")
                .removePrefix("https://")
                .trim()

            val timeout =
                timeoutEdit.text.toString().toIntOrNull() ?: 550

            val newSettings = SettingsModel(
                espAddress = address,
                togglePath = pathEdit.text.toString().trim(),
                mode = if (toggleMode.isChecked) 0 else 1,
                sequence = if (doubleSingle.isChecked) 0 else 1,
                manualTimeout = manualTimeout.isChecked,
                timeout = timeout,
                vibration = vibrationCheck.isChecked,
                showSystemLog = systemLogCheck.isChecked,
                showAudioLog = audioLogCheck.isChecked,
                showNetworkLog = networkLogCheck.isChecked,
                showDebugLog = debugLogCheck.isChecked,
                connectionIndicator = connectionCheck.isChecked
            )

            repository.save(newSettings)

            Toast.makeText(
                this,
                "Settings saved",
                Toast.LENGTH_SHORT
            ).show()
        }

        testButton.setOnClickListener {

            Thread {

                val tester = ConnectionTester(this)

                val success = tester.test()

                runOnUiThread {

                    Toast.makeText(
                        this,
                        if (success) "Request successful" else "Request failed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }.start()
        }
    }
}
