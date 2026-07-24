package com.clap2esp.app

data class SettingsModel(
    val espAddress:String="",
    val togglePath:String="/toggle",
    val mode:Int=0,
    val sequence:Int=0,
    val manualTimeout:Boolean=false,
    val timeout:Int=550,
    val vibration:Boolean=true,
    val networkLog:Boolean=false,
    val connectionIndicator:Boolean=true
)
