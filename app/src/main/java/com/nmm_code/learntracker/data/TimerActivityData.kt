package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TimerActivity(
    var id: Int,
    val seconds: Long,
    val day: Int,
    val year: Int
)

object TimerActivityData : Data<TimerActivity>() {

    fun mergeLast2Weeks(list: List<TimerActivity>): List<TimerActivity> {
        val local = LocalDate.now()

        return merge(list.filter {
            (local.dayOfYear - 14..local.dayOfYear).contains(it.day) && local.year == it.year
        })
    }

    fun merge(list: List<TimerActivity>): List<TimerActivity> {
        return list.groupBy { it.id }
            .map {
                val sumSeconds = it.value.sumOf { elem -> elem.seconds }
                val latest = it.value.maxByOrNull { elem -> elem.year * 1000 + elem.day }!!
                TimerActivity(it.key, sumSeconds, latest.day, latest.year)
            }
    }

    override val fileName: String = "/timer.bin"

    fun getTitleOfIndex(context: Context, index: Int): Subject {
        return SubjectsData.read<Subject>(context)[index]
    }
}