package com.clap2esp.app.settings

data class SettingsModel(

    // ESP
    val espAddress: String = "",

    // HTTP paths
    val togglePath: String = "/toggle",
    val onPath: String = "/on",
    val offPath: String = "/off",

    // Mode
    val mode: Int = 0,

    // Complex OFF sequence
    val sequence: Int = 0,

    // Detection
    val manualTimeout: Boolean = false,
    val timeout: Int = 550,
    val vibration: Boolean = true,

    // Logs
    val showSystemLog: Boolean = true,
    val showAudioLog: Boolean = true,
    val showNetworkLog: Boolean = true,
    val showDebugLog: Boolean = false,

    // UI
    val connectionIndicator: Boolean = true
)
