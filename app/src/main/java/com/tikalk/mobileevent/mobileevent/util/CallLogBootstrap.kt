package com.tikalk.mobileevent.mobileevent.util

import android.content.Context
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.CallLogManager
import java.util.*


class CallLogBootstrap(val context: Context) {

    val random = Random()

    fun bootstrap() {
        val manager = CallLogManager(context)
        manager.deleteAllCallLog()

        for (i in 1..50) {
            manager.write(
                    CallLogDao(
                            i,
                            "05${randomNumber()}",
                            randomDate(),
                            randomDuration(),
                            randomType(),
                            random.nextBoolean(),
                            names[i]))
        }
    }

    fun randomNumber(): Int {
        return random.nextInt(25432465..898765432)
    }

    fun randomDate(): Long {
        val current = System.currentTimeMillis()
        val millisForAWeek = 7 * 24 * 60 * 60 * 1000L
        return random.nextLong(current - millisForAWeek..current)
    }

    fun randomDuration(): Long {
        val minDuration = 20 * 1000L //20sec
        val maxDuration = 2 * 60 * 1000L //2min
        return random.nextLong(minDuration..maxDuration)
    }

    fun randomType(): Int {
        return random.nextInt(1..4)
    }

    companion object {
        val names = mapOf(
                1 to "Pansy Kenner",
                2 to "Palma Backman",
                3 to "Voncile Cacciatore",
                4 to "Tamala Yetter",
                5 to "Joy Spruill",
                6 to "Marilynn Burchell",
                7 to "Gala Mccaa",
                8 to "Nadene Schlosser",
                9 to "Cleotilde Dyson",
                10 to "Kristine Red",
                11 to "Tosha Harvison",
                12 to "Lu Dreiling",
                13 to "Minnie Kennard",
                14 to "Kimi Finlayson",
                15 to "Boris Jensen",
                16 to "Madalene Ham",
                17 to "Fausto Kimrey",
                18 to "Wilburn Schrage",
                19 to "Dominica Vaillancourt",
                20 to "Marnie Thorson",
                21 to "Dee Stanko",
                22 to "Kathyrn Emond",
                23 to "Gail Berwick",
                24 to "Gregorio Eller",
                25 to "Lucius Gearhart",
                26 to "Karissa Christy",
                27 to "Katy Asmussen",
                28 to "Marna Wilcoxen",
                29 to "Teisha Bakewell",
                30 to "Giselle Elwood",
                31 to "Dena Valladares",
                32 to "Frida Bedgood",
                33 to "Alejandro Scoles",
                34 to "Amie Feola",
                35 to "Krystle Digirolamo",
                36 to "Anneliese Caver",
                37 to "Ozella Casas",
                38 to "Shantay Bross",
                39 to "Ignacia Storie",
                40 to "Alfredia Beall",
                41 to "Elwanda Hosford",
                42 to "Ismael Kasel",
                43 to "Magda Wray",
                44 to "Lou Fowles",
                45 to "Raul Greer",
                46 to "Natosha Griffey",
                47 to "Kathaleen Mitton",
                48 to "Diedre Mcclusky",
                49 to "Micah Rushing",
                50 to "Allegra Sattler"
        )
    }
}

fun Random.nextInt(range: IntRange): Int {
    return range.start + nextInt(range.last - range.start)
}

fun Random.nextLong(range: LongRange): Long {
    return range.start + nextLong() % (range.last - range.first)
}