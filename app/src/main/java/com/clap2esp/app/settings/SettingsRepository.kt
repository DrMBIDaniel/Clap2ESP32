package com.clap2esp.app

import android.content.Context

class SettingsRepository(private val context:Context){

    private val prefs=context.getSharedPreferences("clap2esp_settings",Context.MODE_PRIVATE)

    fun save(settings:SettingsModel){
        prefs.edit()
            .putString("espAddress",settings.espAddress)
            .putString("togglePath",settings.togglePath)
            .putInt("mode",settings.mode)
            .putInt("sequence",settings.sequence)
            .putBoolean("manualTimeout",settings.manualTimeout)
            .putInt("timeout",settings.timeout)
            .putBoolean("vibration",settings.vibration)
            .putBoolean("networkLog",settings.networkLog)
            .putBoolean("connectionIndicator",settings.connectionIndicator)
            .apply()
    }

    fun load():SettingsModel{
        return SettingsModel(
            espAddress=prefs.getString("espAddress","") ?: "",
            togglePath=prefs.getString("togglePath","/toggle") ?: "/toggle",
            mode=prefs.getInt("mode",0),
            sequence=prefs.getInt("sequence",0),
            manualTimeout=prefs.getBoolean("manualTimeout",false),
            timeout=prefs.getInt("timeout",550),
            vibration=prefs.getBoolean("vibration",true),
            networkLog=prefs.getBoolean("networkLog",false),
            connectionIndicator=prefs.getBoolean("connectionIndicator",true)
        )
    }
}
