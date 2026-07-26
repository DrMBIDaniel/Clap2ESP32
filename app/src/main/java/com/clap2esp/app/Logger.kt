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

val line = String.format(
    Locale.US,
    "[%-8s][%-3s][%-3s] %s",
    time,
    prefix,
    categoryPrefix,
    message
)

logs.add(line)
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
