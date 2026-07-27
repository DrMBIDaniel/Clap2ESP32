package com.clap2esp.app.command

import android.content.Context
import com.clap2esp.app.LogType
import com.clap2esp.app.Logger
import com.clap2esp.app.network.RequestAgent
import com.clap2esp.app.settings.SettingsRepository

class CommandProcessor(
    context: Context,
    private val requestAgent: RequestAgent
) {

    private val repository =
        SettingsRepository(context)

    private var lastClap: ClapType? = null
    private var lastClapTime = 0L

    private val sequenceTimeout = 1200L

    fun onSingleClap() {

        Logger.log(
            "Single clap received",
            LogType.INFO
        )

        processSequence(ClapType.SINGLE)
    }

    fun onDoubleClap() {

        Logger.log(
            "Double clap received",
            LogType.INFO
        )

        val settings = repository.load()

        Logger.log(
            "Mode = ${if (settings.mode == 0) "TOGGLE" else "ON/OFF"}",
            LogType.INFO
        )

       if (settings.mode == 0) {

    Logger.log(
        "Sending Toggle Path",
        LogType.SUCCESS
    )

    requestAgent.sendToggle()

} else {

    Logger.log(
        "Sending Primary Path",
        LogType.SUCCESS
    )

    requestAgent.sendPrimary()
}

        processSequence(ClapType.DOUBLE)
    }

   fun onSequenceDoubleSingle() {

    val settings = repository.load()

    if (settings.mode == 0) {

        Logger.log(
            "Complex sequences ignored (Toggle mode)",
            LogType.WARNING
        )

        return
    }

    Logger.log(
        "Sequence DOUBLE → SINGLE detected",
        LogType.SUCCESS
    )

    Logger.log(
        "Sending Secondary Path",
        LogType.SUCCESS
    )

    requestAgent.sendSecondary()
}

 fun onSequenceSingleDouble() {

    val settings = repository.load()

    if (settings.mode == 0) {

        Logger.log(
            "Complex sequences ignored (Toggle mode)",
            LogType.WARNING
        )

        return
    }

    Logger.log(
        "Sequence SINGLE → DOUBLE detected",
        LogType.SUCCESS
    )

    Logger.log(
        "Sending Secondary Path",
        LogType.SUCCESS
    )

    requestAgent.sendOff()
}

    fun onLightOn() {

        Logger.log(
            "Light ON",
            LogType.SUCCESS
        )

        requestAgent.sendOn()
    }

    fun onLightOff() {

        Logger.log(
            "Light OFF",
            LogType.SUCCESS
        )

        requestAgent.sendOff()
    }

    private fun processSequence(type: ClapType) {

        val now = System.currentTimeMillis()

        if (lastClap != null &&
            now - lastClapTime < sequenceTimeout
        ) {

            val settings = repository.load()

            Logger.log(
                "Sequence mode = ${settings.sequence}",
                LogType.INFO
            )

            if (
                lastClap == ClapType.DOUBLE &&
                type == ClapType.SINGLE
            ) {

                if (settings.sequence == 0) {

                    onSequenceDoubleSingle()

                    lastClap = null
                    lastClapTime = 0L
                    return

                } else {

                    Logger.log(
                        "DOUBLE → SINGLE ignored",
                        LogType.WARNING
                    )
                }
            }

            if (
                lastClap == ClapType.SINGLE &&
                type == ClapType.DOUBLE
            ) {

                if (settings.sequence == 1) {

                    onSequenceSingleDouble()

                    lastClap = null
                    lastClapTime = 0L
                    return

                } else {

                    Logger.log(
                        "SINGLE → DOUBLE ignored",
                        LogType.WARNING
                    )
                }
            }
        }

        lastClap = type
        lastClapTime = now

        Logger.log(
            "Waiting for second sequence clap",
            LogType.INFO
        )
    }
}
