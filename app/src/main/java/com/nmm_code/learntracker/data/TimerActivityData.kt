package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate

@Serializable
data class TimerActivity(
    var id: Int,
    val seconds: Long,
    val day: Int,
    val year: Int
)

class TimerActivityData : Data<TimerActivity> {
    override fun read(context: Context): List<TimerActivity> {
        var dir = ""
        runBlocking {
            dir = DataStoreState(context, DataStoreState.PATH).get("")
        }

        val file = File(context.filesDir.path + dir + "/timer.json")
        if (!file.exists()) {
            return emptyList()
        }

        val fileContent = file.readText()
        return Json.decodeFromString<List<TimerActivity>>(fileContent)
    }

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

    override fun save(context: Context, list: List<TimerActivity>) {
        var dir = ""
        runBlocking {
            dir = DataStoreState(context, DataStoreState.PATH).get("")
        }

        val file = File(context.filesDir.path + dir + "/timer.json")
        file.printWriter().use { out ->
            out.println(Json.encodeToString(list))
        }
    }

}