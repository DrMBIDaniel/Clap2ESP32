package com.clap2esp.app.settings

import android.content.Context

class SettingsRepository(private val context:Context){

    private val prefs=context.getSharedPreferences("clap2esp_settings",Context.MODE_PRIVATE)

    fun save(ip:String,path:String){
        prefs.edit()
            .putString("ip",ip)
            .putString("path",path)
            .apply()
    }

    fun load():SettingsModel{
        return SettingsModel(
            ip=prefs.getString("ip","") ?: "",
            path=prefs.getString("path","/toggle") ?: "/toggle"
        )
    }
}
