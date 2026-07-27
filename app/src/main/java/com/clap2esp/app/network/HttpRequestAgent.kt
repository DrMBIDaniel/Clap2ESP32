package com.clap2esp.app.network

import android.content.Context
import com.clap2esp.app.Logger
import com.clap2esp.app.settings.SettingsRepository
import java.net.HttpURLConnection
import java.net.URL
import com.clap2esp.app.LogType
import com.clap2esp.app.LogCategory

class HttpRequestAgent(
    context: Context
) : RequestAgent {

    private val repository = SettingsRepository(context)

    override fun sendToggle(): Boolean {

    return sendRequest("toggle")

}

override fun sendPrimary(): Boolean {

    return sendRequest("primary")

}

override fun sendSecondary(): Boolean {

    return sendRequest("secondary")

}

    private fun sendRequest(command: String): Boolean {

        return try {

            val settings = repository.load()

            val address = settings.espAddress

            val url = when (command) {

    "toggle" ->
        "http://$address${settings.togglePath}"

    "primary" ->
        "http://$address${settings.onPath}"

    "secondary" ->
        "http://$address${settings.offPath}"

    else ->
        "http://$address${settings.togglePath}"
}

           Logger.log(
    "GET $command",
    LogType.INFO,
    LogCategory.NET
)
           
            val connection =
                URL(url).openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            val code = connection.responseCode

            if (code == 200) {
    Logger.log(
    "HTTP $code",
    LogType.SUCCESS,
    LogCategory.NET
)
    
} else {
    Logger.log(
    "HTTP $code",
    LogType.WARNING,
    LogCategory.NET
)
}

            connection.disconnect()

            code == 200

        } 
        
        catch (e: Exception) {

    Logger.log(
    "HTTP ${e.javaClass.simpleName}",
    LogType.ERROR,
    LogCategory.NET
)

    false
}

    }

}
