package com.clap2esp.app.network

import android.content.Context

class ConnectionTester(
    context: Context
) {

    private val agent = HttpRequestAgent(context)

    fun test(): Boolean {

        return agent.sendToggle()

    }

}
