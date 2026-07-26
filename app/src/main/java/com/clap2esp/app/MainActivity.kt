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
import android.widget.Toast

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

        Logger.log(
    "Application started",
    LogType.INFO,
    LogCategory.SYS
)

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

           Logger.log(
    "Start button pressed",
    LogType.INFO,
    LogCategory.SYS
)
        }

        stopButton.setOnClickListener {

            val serviceIntent = Intent(
                this,
                AudioService::class.java
            )

            stopService(serviceIntent)

            Logger.log(
    "Stop button pressed",
    LogType.INFO,
    LogCategory.SYS
)
            
        }

        settingsButton.setOnClickListener{
    startActivity(Intent(this,SettingsActivity::class.java))
}
        testButton.setOnClickListener{
     Logger.log(
    "Test button pressed",
    LogType.INFO,
    LogCategory.SYS
)
}

        copyLogButton.setOnClickListener {

    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    val clip = ClipData.newPlainText(
        "Log",
        Logger.getLogs()
    )

    clipboard.setPrimaryClip(clip)

    Logger.log(
    "Log copied",
    LogType.SUCCESS,
    LogCategory.SYS
)
    android.widget.Toast.makeText(
    this,
    "Log copied to clipboard",
    android.widget.Toast.LENGTH_SHORT
).show()

}
        clearLogButton.setOnClickListener {

    Logger.clear()

Logger.log(
    "Log cleared",
    LogType.INFO,
    LogCategory.SYS
)

Toast.makeText(
    this,
    "Log cleared",
    Toast.LENGTH_SHORT
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

           Logger.log(
    "Microphone permission granted",
    LogType.SUCCESS,
    LogCategory.SYS
)
        } else {

            Logger.log(
    "Requesting microphone permission",
    LogType.WARNING,
    LogCategory.SYS
)

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

        val timeEnd =
            line.indexOf("]") + 1

        if (timeEnd > 0) {
            builder.setSpan(
                ForegroundColorSpan(Color.GRAY),
                start,
                start + timeEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val typeStart = line.indexOf("[", timeEnd)
        val typeEnd = line.indexOf("]", typeStart) + 1

        if (typeStart >= 0 && typeEnd > typeStart) {

            val color = when {

                "[INF]" in line ->
                    Color.rgb(80,180,255)

                "[OK ]" in line ->
                    Color.rgb(0,220,0)

                "[WRN]" in line ->
                    Color.rgb(255,180,0)

                "[ERR]" in line ->
                    Color.rgb(255,70,70)

                "[DBG]" in line ->
                    Color.rgb(180,180,180)

                else ->
                    Color.WHITE
            }

            builder.setSpan(
                ForegroundColorSpan(color),
                start + typeStart,
                start + typeEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val catStart = line.indexOf("[", typeEnd)
        val catEnd = line.indexOf("]", catStart) + 1

        if (catStart >= 0 && catEnd > catStart) {

            val color = when {

                "[SYS]" in line ->
                    Color.rgb(255,255,255)

                "[AUD]" in line ->
                    Color.rgb(170,120,255)

                "[NET]" in line ->
                    Color.rgb(0,255,255)

                "[DBG]" in line ->
                    Color.rgb(140,140,140)

                else ->
                    Color.WHITE
            }

            builder.setSpan(
                ForegroundColorSpan(color),
                start + catStart,
                start + catEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val messageStart =
            line.indexOf("]", catEnd) + 2

        if (messageStart > 1 && messageStart < line.length) {

            builder.setSpan(
                ForegroundColorSpan(Color.WHITE),
                start + messageStart,
                start + line.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    }

    return builder
}
   
}
