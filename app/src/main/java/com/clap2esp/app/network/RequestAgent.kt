package com.clap2esp.app.network

interface RequestAgent {

    fun sendToggle(): Boolean

    fun sendOn(): Boolean

    fun sendOff(): Boolean

}
