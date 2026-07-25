package com.clap2esp.app.command

import android.content.Context
import com.clap2esp.app.Logger
import com.clap2esp.app.network.RequestAgent
import com.clap2esp.app.settings.SettingsRepository

class CommandProcessor(
    context: Context,
    private val requestAgent: RequestAgent
) {

    private val repository =
        SettingsRepository(context)

    private var lightState = false
    private var lastClap: ClapType? = null
private var lastClapTime = 0L

private val sequenceTimeout = 1200L

    fun onSingleClap() {

    Logger.log("Single clap")

    processSequence(ClapType.SINGLE)
}

    fun onDoubleClap() {

    Logger.log("Double clap")

    val settings = repository.load()

    if (settings.mode == 0) {

        requestAgent.sendToggle()

        return
    }

    lightState = !lightState

    if (lightState) {

        requestAgent.sendOn()

    } else {

        requestAgent.sendOff()
        processSequence(ClapType.DOUBLE)

    }

}

    fun onSequenceDoubleSingle() {
        Logger.log("Double → Single")
        requestAgent.sendOff()
    }

    fun onSequenceSingleDouble() {
        Logger.log("Single → Double")
        requestAgent.sendOff()
    }

    fun onLightOn() {
        Logger.log("Light ON")
        requestAgent.sendOn()
    }

    fun onLightOff() {
        Logger.log("Light OFF")
        requestAgent.sendOff()
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
            }

        }

        if (
            lastClap == ClapType.SINGLE &&
            type == ClapType.DOUBLE
        ) {

            if (settings.sequence == 1) {

                Logger.log("Sequence: SINGLE -> DOUBLE")

                onSequenceSingleDouble()
            }

        }

    }

    lastClap = type
    lastClapTime = now
}
}
