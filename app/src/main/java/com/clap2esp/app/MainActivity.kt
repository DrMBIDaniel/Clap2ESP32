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

        // ---------- Время ----------
        if (line.length >= 10) {

            builder.setSpan(
                ForegroundColorSpan(Color.rgb(140,140,140)),
                start,
                start + 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        // ---------- Тип ----------
        when {

            line.contains("[INF]") -> colorTag(
                builder,
                line,
                start,
                "[INF]",
                Color.rgb(70,170,255)
            )

            line.contains("[OK ]") -> colorTag(
                builder,
                line,
                start,
                "[OK ]",
                Color.rgb(0,220,80)
            )

            line.contains("[WRN]") -> colorTag(
                builder,
                line,
                start,
                "[WRN]",
                Color.rgb(255,170,0)
            )

            line.contains("[ERR]") -> colorTag(
                builder,
                line,
                start,
                "[ERR]",
                Color.rgb(255,70,70)
            )

            line.contains("[DBG]") -> colorTag(
                builder,
                line,
                start,
                "[DBG]",
                Color.rgb(210,120,255)
            )

        }

        // ---------- Категория ----------

        when {

            line.contains("[SYS]") -> colorTag(
                builder,
                line,
                start,
                "[SYS]",
                Color.rgb(0,200,255)
            )

            line.contains("[AUD]") -> colorTag(
                builder,
                line,
                start,
                "[AUD]",
                Color.rgb(255,220,0)
            )

            line.contains("[NET]") -> colorTag(
                builder,
                line,
                start,
                "[NET]",
                Color.rgb(0,255,200)
            )

            line.contains("[DBG]") -> colorTag(
                builder,
                line,
                start,
                "[DBG]",
                Color.rgb(200,120,255)
            )

        }

    }

    return builder

}

    private fun colorTag(

    builder: SpannableStringBuilder,
    line: String,
    start: Int,
    tag: String,
    color: Int

) {

    val index = line.indexOf(tag)

    if (index == -1) return

    builder.setSpan(

        ForegroundColorSpan(color),

        start + index,

        start + index + tag.length,

        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

    )

}
            
}
