package com.clap2esp.app.settings

import android.content.Context

class SettingsRepository(private val context:Context){

    private val prefs=context.getSharedPreferences("clap2esp_settings",Context.MODE_PRIVATE)

    fun save(settings:SettingsModel){
        prefs.edit()
            .putString("espAddress",settings.espAddress)
            .putString("togglePath",settings.togglePath)
            .putString("onPath",settings.onPath)
            .putString("offPath",settings.offPath)
            
            .putInt("mode",settings.mode)
            .putInt("sequence",settings.sequence)
            .putBoolean("manualTimeout",settings.manualTimeout)
            .putInt("timeout",settings.timeout)
            .putBoolean("vibration",settings.vibration)
            .putBoolean("showSystemLog", settings.showSystemLog)
            .putBoolean("showAudioLog", settings.showAudioLog)
            .putBoolean("showNetworkLog", settings.showNetworkLog)
            .putBoolean("showDebugLog", settings.showDebugLog)
            .putBoolean("connectionIndicator",settings.connectionIndicator)
            .apply()
    }

    fun load():SettingsModel{
        return SettingsModel(
            espAddress = prefs.getString("espAddress","") ?: "",
            togglePath = prefs.getString("togglePath","/toggle") ?: "/toggle",

            onPath = prefs.getString("onPath","/on") ?: "/on",
            offPath = prefs.getString("offPath","/off") ?: "/off",
            
            mode=prefs.getInt("mode",0),
            sequence=prefs.getInt("sequence",0),
            manualTimeout=prefs.getBoolean("manualTimeout",false),
            timeout=prefs.getInt("timeout",550),
            vibration=prefs.getBoolean("vibration",true),
          
            showSystemLog =
    prefs.getBoolean("showSystemLog", true),

showAudioLog =
    prefs.getBoolean("showAudioLog", true),

showNetworkLog =
    prefs.getBoolean("showNetworkLog", true),

showDebugLog =
    prefs.getBoolean("showDebugLog", false),
            connectionIndicator=prefs.getBoolean("connectionIndicator",true)
        )
    }
}
