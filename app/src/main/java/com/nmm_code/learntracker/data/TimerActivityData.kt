package com.nmm_code.learntracker.data

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TimerActivity(
    var id: Int,
    val seconds: Long,
    val day: Int,
    val year: Int
)

class TimerActivityData : Data<TimerActivity>() {

    companion object {
        fun mergeLast2Weeks(list: List<TimerActivity>): List<TimerActivity> {
            val local = LocalDate.now()

            return list
                .filter {
                    (local.dayOfYear - 14..local.dayOfYear).contains(it.day) && local.year == it.year
                }.groupBy { it.id }
                .map {
                    val sumSeconds = it.value.sumOf { elem -> elem.seconds }
                    val latest = it.value.maxByOrNull { elem -> elem.year * 1000 + elem.day }!!
                    TimerActivity(it.key, sumSeconds, latest.day, latest.year)
                }

        }
    }

    override val fileName: String = "/timer.bin"

}