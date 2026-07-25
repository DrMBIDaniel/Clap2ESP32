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
    private val repository =
    SettingsRepository(context)

    private val threshold = 12000

    private fun getDoubleClapInterval(): Long {

    val settings = repository.load()

    return if (settings.manualTimeout) {
        settings.timeout.toLong()
    } else {
        800L
    }

}
    
    private val minClapInterval = 150L

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
    "First clap amplitude=$maxAmplitude",
    LogType.INFO
)



            return ClapType.NONE

        }





        val delay =
            currentTime - firstClapTime




        if (delay <= getDoubleClapInterval()) {


            waitingSecondClap = false



            Logger.log(
    "Double clap delay=${delay}ms",
    LogType.SUCCESS
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
    LogType.INFO
)


            return ClapType.SINGLE_CLAP

        }


        return ClapType.NONE

    }

}
