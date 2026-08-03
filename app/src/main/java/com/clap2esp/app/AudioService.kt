package com.clap2esp.app


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.clap2esp.app.command.CommandProcessor
import com.clap2esp.app.network.HttpRequestAgent
import com.clap2esp.app.LogType
import com.clap2esp.app.LogCategory
import com.clap2esp.app.settings.SettingsRepository
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class AudioService : Service() {

    private val mainHandler =
    android.os.Handler(
        android.os.Looper.getMainLooper()
    )

    private lateinit var repository: SettingsRepository

    private fun vibrate() {

    val settings = repository.load()

    if (!settings.vibration) return

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        val manager =
            getSystemService(VIBRATOR_MANAGER_SERVICE)
                    as VibratorManager

        manager.defaultVibrator

    } else {

        @Suppress("DEPRECATION")
        getSystemService(VIBRATOR_SERVICE) as Vibrator

    }

    if (!vibrator.hasVibrator()) {
    Logger.log(
        "Device has no vibrator",
        LogType.WARNING,
        LogCategory.SYS
    )
    return
}

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        vibrator.vibrate(
            VibrationEffect.createOneShot(
                240,
                255
            )
        )

    } else {

        @Suppress("DEPRECATION")
        vibrator.vibrate(40)

    }
}

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private lateinit var clapDetector: ClapDetector
    private lateinit var requestAgent: HttpRequestAgent
    private lateinit var commandProcessor: CommandProcessor
        
    private val channelId =
        "Clap2ESP_Channel"

    override fun onCreate() {

       super.onCreate()

        repository = SettingsRepository(this)

        Logger.initialize(repository)
       
        clapDetector = ClapDetector(this)

Logger.log(
    "AudioService created",
    LogType.INFO,
    LogCategory.SYS
)

requestAgent = HttpRequestAgent(this)

commandProcessor =
    CommandProcessor(
        this,
        requestAgent
    )
createNotificationChannel()

        val notification =
            Notification.Builder(
                this,
                channelId
            )
                .setContentTitle(
                    "Clap2ESP"
                )
                .setContentText(
                    "Listening for claps..."
                )
                .setSmallIcon(
                    android.R.drawable.ic_menu_info_details
                )
                .build()

        startForeground(
            1,
            notification
        )

        Logger.log(
    "Foreground service started",
    LogType.SUCCESS,
    LogCategory.SYS
)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        startListening()

        return START_NOT_STICKY

    }

    private fun startListening() {

        if (isRecording) {

            return

        }

        val bufferSize =
            AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

        audioRecord =
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

        audioRecord?.startRecording()
        Logger.log(
    "44100 Hz | PCM16 | Mono",
    LogType.INFO,
    LogCategory.AUD
)
        
        isRecording = true

        Logger.log(
    "Listening started",
    LogType.SUCCESS,
    LogCategory.AUD
)
        
        Thread {

            val buffer =
                ShortArray(bufferSize)

            while(isRecording) {

                val read =
                    audioRecord?.read(
                        buffer,
                        0,
                        buffer.size
                    )

                if (
                    read != null &&
                    read > 0
                ) {

                    when(
                        clapDetector.detect(buffer)
                    ) {

                        ClapType.DOUBLE_CLAP -> {

                            Logger.log(
                                "DOUBLE CLAP",
                                LogType.SUCCESS,
                                LogCategory.AUD
                                )
                            
                            commandProcessor.onDoubleClap()
                            
                           mainHandler.post {
                               

    try {

        vibrate()

    } catch (e: Exception) {

    }

}

                            sendBroadcast(
                                Intent("DOUBLE_CLAP")
                            )

                        }

                        else -> {

                        }

                    }

                    when(
                        clapDetector.checkSingleClapTimeout()
                    ) {

                        ClapType.SINGLE_CLAP -> {

                          Logger.log(
                        "SINGLE CLAP",
                        LogType.INFO,
                        LogCategory.AUD
                        )
                          
                            commandProcessor.onSingleClap()

                            sendBroadcast(
                                Intent("SINGLE_CLAP")
                            )

                        }

                        else -> {

                        }

                    }

                }

            }

        }.start()

    }

    private fun createNotificationChannel() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Clap2ESP Listener",
                    NotificationManager.IMPORTANCE_LOW
                )

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                channel
            )

        }

    }

    override fun onDestroy() {

        Logger.log(
    "Listening stopped",
    LogType.WARNING,
    LogCategory.SYS
)
        
        isRecording = false
        try {

            audioRecord?.stop()

        } catch(e: Exception) {

            Log.e(
                "CLAP",
                "Stop error"
            )

        }

        audioRecord?.release()
        audioRecord = null
        super.onDestroy()

    }

    override fun onBind(intent: Intent?): IBinder? {

        return null

    }

}
