package com.clap2esp.app.network

import android.content.Context
import com.clap2esp.app.LogCategory
import com.clap2esp.app.LogType
import com.clap2esp.app.Logger
import com.clap2esp.app.settings.SettingsRepository

class ConnectionTester(
    context: Context
) {

    private val requestAgent = HttpRequestAgent(context)

    fun testToggle(): Boolean {

        Logger.log(
            "Testing Toggle command",
            LogType.INFO,
            LogCategory.NET
        )

        return requestAgent.sendToggle()
    }

    fun testPrimary(): Boolean {

        Logger.log(
            "Testing Primary command",
            LogType.INFO,
            LogCategory.NET
        )

        return requestAgent.sendOn()
    }

    fun testSecondary(): Boolean {

        Logger.log(
            "Testing Secondary command",
            LogType.INFO,
            LogCategory.NET
        )

        return requestAgent.sendOff()
    }

    fun test(): Boolean {
        return testToggle()
    }
}
