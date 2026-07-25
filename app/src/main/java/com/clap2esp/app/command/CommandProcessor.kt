package com.clap2esp.app.command

import com.clap2esp.app.Logger
import com.clap2esp.app.network.RequestAgent
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

    fun onSingleClap() {
        Logger.log("Single clap")
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
}
