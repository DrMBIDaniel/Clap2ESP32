package com.clap2esp.app.network

import android.content.Context
import com.clap2esp.app.SettingsRepository

class HttpRequestAgent(
    context: Context
) : RequestAgent {

    private val repository = SettingsRepository(context)

    override fun sendToggle(): Boolean {

        val settings = repository.load()

        println(settings.espAddress)
        println(settings.togglePath)

        return true
    }

    override fun sendOn(): Boolean {

        return true
    }

    override fun sendOff(): Boolean {

        return true
    }
}
