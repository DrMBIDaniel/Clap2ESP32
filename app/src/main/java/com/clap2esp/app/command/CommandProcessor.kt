package com.clap2esp.app

class CommandProcessor(
    private val requestAgent: RequestAgent
) {

    fun onSingleClap() {

        Logger.log("Single clap")

    }

    fun onDoubleClap() {

        Logger.log("Double clap")

        requestAgent.sendToggle()

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
