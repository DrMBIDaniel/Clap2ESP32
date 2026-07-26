package com.clap2esp.app

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LogType {
    INFO,
    SUCCESS,
    WARNING,
    ERROR
}

enum class LogCategory {
    SYS,
    AUD,
    NET,
    DBG
}

object Logger {

    private val logs = mutableListOf<String>()

    private var listener: (() -> Unit)? = null

    fun setOnLogChanged(callback: () -> Unit) {
        listener = callback
    }

    fun log(
    message: String,
    type: LogType = LogType.INFO,
    category: LogCategory = LogCategory.SYS
) {

    val time = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    ).format(Date())

    val prefix = when(type){
    LogType.INFO -> "INF"
    LogType.SUCCESS -> "OK "
    LogType.WARNING -> "WRN"
    LogType.ERROR -> "ERR"
}

    val categoryPrefix = when (category) {

    LogCategory.SYS -> "SYS"

    LogCategory.AUD -> "AUD"

    LogCategory.NET -> "NET"

    LogCategory.DBG -> "DBG"

}

logs.add("[$time][$prefix][$categoryPrefix] $message")
    while (logs.size > 300) {
        logs.removeAt(0)
    }

    listener?.invoke()
}

    fun getLogs(): String {

        return logs.joinToString("\n")

    }

    fun clear() {

        logs.clear()

        listener?.invoke()

    }
}
