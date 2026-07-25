package com.clap2esp.app.command

import android.content.Context
import com.clap2esp.app.Logger
import com.clap2esp.app.network.RequestAgent
import com.clap2esp.app.settings.SettingsRepository
import com.clap2esp.app.LogType

class CommandProcessor(
    context: Context,
private val requestAgent: RequestAgent
) {

private val repository =
        SettingsRepository(context)

private var lastClap: ClapType? = null
private var lastClapTime = 0L

private val sequenceTimeout = 1200L
private fun executeRequest(action: () -> Boolean) {

    Thread {

        val success = action()

        if (success) {
            Logger.log("HTTP command successful")
        } else {
            Logger.log("HTTP command failed")
        }

    }.start()

}

    fun onSingleClap() {

    Logger.log(
    "Single clap",
    LogType.INFO
)

    processSequence(ClapType.SINGLE)
}

    fun onDoubleClap() {

    Logger.log(
    "Double clap",
    LogType.INFO
)

    val settings = repository.load()

    if (settings.mode == 0) {

    executeRequest {
        requestAgent.sendToggle()
    }

} else {

    executeRequest {
        requestAgent.sendOn()
    }

}

    processSequence(ClapType.DOUBLE)

}

    fun onSequenceDoubleSingle() {
        Logger.log(
    "Sequence: DOUBLE -> SINGLE",
    LogType.SUCCESS
)
        executeRequest {
    requestAgent.sendOff()
}
    }

    fun onSequenceSingleDouble() {
        Logger.log(
    "Sequence: SINGLE -> DOUBLE",
    LogType.SUCCESS
)
        executeRequest {
    requestAgent.sendOff()
}
    }

    fun onLightOn() {
        Logger.log(
    "Light ON",
    LogType.SUCCESS
)
        executeRequest {
    requestAgent.sendOn()
}
    }

    fun onLightOff() {
        Logger.log(
    "Light OFF",
    LogType.SUCCESS
)
        executeRequest {
    requestAgent.sendOff()
}
    }

    private fun processSequence(type: ClapType) {

    val now = System.currentTimeMillis()

    if (lastClap != null &&
        now - lastClapTime < sequenceTimeout
    ) {

        val settings = repository.load()

        if (
            lastClap == ClapType.DOUBLE &&
            type == ClapType.SINGLE
        ) {

            if (settings.sequence == 0) {

    Logger.log("Sequence: DOUBLE -> SINGLE")

    onSequenceDoubleSingle()

    lastClap = null
    lastClapTime = 0L

    return
}

        }

        if (
            lastClap == ClapType.SINGLE &&
            type == ClapType.DOUBLE
        ) {

            if (settings.sequence == 1) {

    Logger.log("Sequence: SINGLE -> DOUBLE")

    onSequenceSingleDouble()

    lastClap = null
    lastClapTime = 0L

    return
}
        }

    }

    lastClap = type
    lastClapTime = now
}
}
