package com.clap2esp.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import android.widget.TextView
import android.content.ClipData
import android.content.ClipboardManager
import com.clap2esp.app.settings.SettingsActivity
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.graphics.Typeface
import android.text.style.StyleSpan

class MainActivity : AppCompatActivity() {

    private val microphonePermissionCode = 100
    private lateinit var logText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        logText = findViewById(R.id.logText)

Logger.setOnLogChanged {

    runOnUiThread {

        logText.text = buildColoredLog()

        logText.post {

    val layout = logText.layout

    if (layout != null) {

        val scroll =
            layout.getLineTop(logText.lineCount) - logText.height

        if (scroll > 0)
            logText.scrollTo(0, scroll)
        else
            logText.scrollTo(0, 0)

    }

}

    }

}

        Logger.log("MainActivity created")

        checkMicrophonePermission()

val startButton=findViewById<Button>(R.id.startButton)
val stopButton=findViewById<Button>(R.id.stopButton)
val settingsButton=findViewById<Button>(R.id.settingsButton)
val testButton=findViewById<Button>(R.id.testButton)
val copyLogButton=findViewById<Button>(R.id.copyLogButton)
val clearLogButton=findViewById<Button>(R.id.clearLogButton)

        startButton.setOnClickListener {

            val serviceIntent = Intent(
                this,
                AudioService::class.java
            )

            startForegroundService(
                this,
                serviceIntent
            )

            Logger.log("Start button pressed")
        }

        stopButton.setOnClickListener {

            val serviceIntent = Intent(
                this,
                AudioService::class.java
            )

            stopService(serviceIntent)

            Logger.log("Stop button pressed")
        }

        settingsButton.setOnClickListener{
    startActivity(Intent(this,SettingsActivity::class.java))
}
        testButton.setOnClickListener{
    Logger.log("Test button pressed")
}

        copyLogButton.setOnClickListener {

    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    val clip = ClipData.newPlainText(
        "Log",
        Logger.getLogs()
    )

    clipboard.setPrimaryClip(clip)

    Logger.log("Log copied")
    android.widget.Toast.makeText(
    this,
    "Log copied to clipboard",
    android.widget.Toast.LENGTH_SHORT
).show()

}
        clearLogButton.setOnClickListener {

    Logger.clear()
    android.widget.Toast.makeText(
    this,
    "Log cleared",
    android.widget.Toast.LENGTH_SHORT
).show()

}

    }

    private fun checkMicrophonePermission() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            Logger.log("Microphone permission granted")

        } else {

            Logger.log("Requesting microphone permission")

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO
                ),
                microphonePermissionCode
            )
        }
    }

    private fun buildColoredLog(): SpannableStringBuilder {

    val builder = SpannableStringBuilder()

    val lines = Logger.getLogs().split("\n")

    for (line in lines) {

        if (line.isBlank()) continue

        val start = builder.length

        builder.append(line)
        builder.append("\n")

        //-----------------------
        // Цвет времени
        //-----------------------

        val timeEnd = line.indexOf("]")

        if (timeEnd != -1) {

            builder.setSpan(
                ForegroundColorSpan(Color.rgb(120,120,120)),
                start,
                start + timeEnd + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        //-----------------------
        // Цвет типа сообщения
        //-----------------------

        val secondOpen = line.indexOf("[", timeEnd + 1)
        val secondClose = line.indexOf("]", secondOpen)

        if (secondOpen != -1 && secondClose != -1) {

            val tag = line.substring(secondOpen + 1, secondClose)

            val color = when(tag.trim()) {

                "INF" ->
                    Color.rgb(70,170,255)

                "OK" ->
                    Color.rgb(0,220,0)

                "WRN" ->
                    Color.rgb(255,170,0)

                "ERR" ->
                    Color.rgb(255,60,60)

                else ->
                    Color.WHITE
            }

            builder.setSpan(
                ForegroundColorSpan(color),
                start + secondOpen,
                start + secondClose + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        //-----------------------
        // Цвет категории
        //-----------------------

        val thirdOpen = line.indexOf("[", secondClose + 1)
        val thirdClose = line.indexOf("]", thirdOpen)

        if (thirdOpen != -1 && thirdClose != -1) {

            val tag = line.substring(thirdOpen + 1, thirdClose)

            val color = when(tag) {

                "SYS" ->
                    Color.WHITE

                "AUD" ->
                    Color.rgb(0,255,255)

                "NET" ->
                    Color.rgb(210,120,255)

                "DBG" ->
                    Color.rgb(255,255,0)

                else ->
                    Color.WHITE

            }

            builder.setSpan(
                ForegroundColorSpan(color),
                start + thirdOpen,
                start + thirdClose + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        //-----------------------
        // Цвет текста сообщения
        //-----------------------

        val messageStart = line.indexOf(" ", thirdClose)

        if (messageStart != -1) {

            builder.setSpan(
                ForegroundColorSpan(Color.WHITE),
                start + messageStart + 1,
                start + line.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

    }

    return builder

}
            
}
