package com.clap2esp.app

import kotlin.math.abs
import android.content.Context
import com.clap2esp.app.settings.SettingsRepository
import com.clap2esp.app.LogType

enum class ClapType {
    NONE,
    SINGLE_CLAP,
    DOUBLE_CLAP
}

class ClapDetector(
    context: Context
) {

private companion object {

    const val DEFAULT_THRESHOLD = 12000
    const val DEFAULT_DOUBLE_CLAP_TIMEOUT = 800L
    const val MIN_CLAP_INTERVAL = 150L

}
    
    private val repository =
    SettingsRepository(context)

    private val threshold = DEFAULT_THRESHOLD
    
   private fun getDoubleClapInterval(): Long {

    val settings = repository.load()

    return if (settings.manualTimeout)
        settings.timeout.toLong()
    else
        DEFAULT_DOUBLE_CLAP_TIMEOUT

}
    
    private val minClapInterval = MIN_CLAP_INTERVAL

    private var firstClapTime = 0L

    private var waitingSecondClap = false

    private var lastDetectionTime = 0L

    fun detect(buffer: ShortArray): ClapType {

        var maxAmplitude = 0

        for (sample in buffer) {

            val amplitude =
                abs(sample.toInt())

            if (amplitude > maxAmplitude) {

                maxAmplitude = amplitude

            }

        }

        val currentTime =
            System.currentTimeMillis()

        if (maxAmplitude < threshold) {

            return ClapType.NONE

        }

        if (
            currentTime - lastDetectionTime
            < minClapInterval
        ) {

            return ClapType.NONE

        }
        
        lastDetectionTime = currentTime

        if (!waitingSecondClap) {
            waitingSecondClap = true
            firstClapTime = currentTime

           Logger.log(
    "First clap detected (amp=$maxAmplitude)",
    LogType.INFO,
    LogCategory.AUD
)
           
            return ClapType.NONE

        }

        val delay =
            currentTime - firstClapTime

        if (delay <= getDoubleClapInterval()) {
            waitingSecondClap = false

            Logger.log(
    "Double clap detected (${delay} ms)",
    LogType.SUCCESS,
    LogCategory.AUD
)

            return ClapType.DOUBLE_CLAP

        }

        firstClapTime = currentTime

        return ClapType.NONE

    }

    fun checkSingleClapTimeout(): ClapType {

        if (
            waitingSecondClap &&
            System.currentTimeMillis()
- firstClapTime > getDoubleClapInterval()
        ) {
            waitingSecondClap = false

           Logger.log(
    "Single clap detected",
    LogType.SUCCESS,
    LogCategory.AUD
)

            return ClapType.SINGLE_CLAP

        }

        return ClapType.NONE

    }

}
