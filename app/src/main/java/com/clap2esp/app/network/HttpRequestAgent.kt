package com.clap2esp.app.network

import android.content.Context
import com.clap2esp.app.Logger
import com.clap2esp.app.settings.SettingsRepository
import java.net.HttpURLConnection
import java.net.URL

class HttpRequestAgent(
    context: Context
) : RequestAgent {

    private val repository = SettingsRepository(context)

    override fun sendToggle(): Boolean {

        return sendRequest("toggle")

    }

    override fun sendOn(): Boolean {

        return sendRequest("on")

    }

    override fun sendOff(): Boolean {

        return sendRequest("off")

    }

    private fun sendRequest(command: String): Boolean {

        return try {

            val settings = repository.load()

            val address = settings.espAddress

            val url = when (command) {
                "toggle" -> "http://$address${settings.togglePath}"
                "on" -> "http://$address/on"
                else -> "http://$address/off"
            }

            Logger.log("HTTP: $url")

            val connection =
                URL(url).openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            val code = connection.responseCode

            Logger.log("HTTP Response: $code")

            connection.disconnect()

            code == 200

        } 
        
        catch (e: Exception) {

    Logger.log("HTTP Error: ${e.javaClass.simpleName}")

    Logger.log(e.message ?: "No message")

    false
}

    }

}
