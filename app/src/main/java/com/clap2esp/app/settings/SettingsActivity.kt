package com.clap2esp.app.settings

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.clap2esp.app.R
import com.clap2esp.app.network.ConnectionTester
import android.view.View

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val repository = SettingsRepository(this)

        val ipEdit = findViewById<EditText>(R.id.ipEdit)
        val togglePathEdit = findViewById<EditText>(R.id.togglePathEdit)
        val primaryPathEdit = findViewById<EditText>(R.id.primaryPathEdit)
        val secondaryPathEdit = findViewById<EditText>(R.id.secondaryPathEdit)
        
        val toggleMode = findViewById<RadioButton>(R.id.toggleMode)
        val onOffMode = findViewById<RadioButton>(R.id.onOffMode)
        val toggleLayout = findViewById<LinearLayout>(R.id.toggleLayout)
        val onOffLayout = findViewById<LinearLayout>(R.id.onOffLayout)
        val sequenceLayout = findViewById<LinearLayout>(R.id.sequenceLayout)

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
        val testPrimaryButton = findViewById<Button>(R.id.testPrimaryButton)
        val testSecondaryButton = findViewById<Button>(R.id.testSecondaryButton)

        val backButton = findViewById<Button>(R.id.backButton)

backButton.setOnClickListener {
    finish()
}

        val settings = repository.load()

        ipEdit.setText(settings.espAddress)
        togglePathEdit.setText(settings.togglePath)
        primaryPathEdit.setText(settings.onPath)
        secondaryPathEdit.setText(settings.offPath)

        toggleMode.isChecked = settings.mode == 0
        onOffMode.isChecked = settings.mode == 1

fun updateModeUI() {

    if (toggleMode.isChecked) {

        toggleLayout.visibility = View.VISIBLE
        onOffLayout.visibility = View.GONE
        sequenceLayout.visibility = View.GONE

    } else {

        toggleLayout.visibility = View.GONE
        onOffLayout.visibility = View.VISIBLE
        sequenceLayout.visibility = View.VISIBLE

    }
}

updateModeUI()
        
        doubleSingle.isChecked = settings.sequence == 0
        singleDouble.isChecked = settings.sequence == 1
        toggleMode.setOnCheckedChangeListener { _, _ ->
    updateModeUI()
}

        onOffMode.setOnCheckedChangeListener { _, _ ->
    updateModeUI()
}

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
                togglePath = togglePathEdit.text.toString().trim(),
                onPath = primaryPathEdit.text.toString().trim(),
                offPath = secondaryPathEdit.text.toString().trim(),
                
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

        val success = ConnectionTester(this).testToggle()

        runOnUiThread {

            Toast.makeText(
                this,
                if (success) "Toggle request successful"
                else "Toggle request failed",
                Toast.LENGTH_SHORT
            ).show()

        }

    }.start()
}

testPrimaryButton.setOnClickListener {

    Thread {

        val success = ConnectionTester(this).testPrimary()

        runOnUiThread {

            Toast.makeText(
                this,
                if (success) "Primary request successful"
                else "Primary request failed",
                Toast.LENGTH_SHORT
            ).show()

        }

    }.start()
}

testSecondaryButton.setOnClickListener {

    Thread {

        val success = ConnectionTester(this).testSecondary()

        runOnUiThread {

            Toast.makeText(
                this,
                if (success) "Secondary request successful"
                else "Secondary request failed",
                Toast.LENGTH_SHORT
            ).show()

        }

    }.start()
}
    }
}
