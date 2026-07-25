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
object Logger {

    private val logs = mutableListOf<String>()

    private var listener: (() -> Unit)? = null

    fun setOnLogChanged(callback: () -> Unit) {
        listener = callback
    }

    fun log(
    message: String,
    type: LogType = LogType.INFO
) {

    val time = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    ).format(Date())

    val prefix = when(type){
    LogType.INFO -> "INFO"
    LogType.SUCCESS -> "OK"
    LogType.WARNING -> "WARN"
    LogType.ERROR -> "ERROR"
}

logs.add("[$time][$prefix] $message")

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
