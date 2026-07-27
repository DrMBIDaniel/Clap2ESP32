package com.clap2esp.app.network

interface RequestAgent {

    fun sendToggle(): Boolean

    fun sendPrimary(): Boolean

    fun sendSecondary(): Boolean
}
