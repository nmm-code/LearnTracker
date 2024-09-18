package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.DayOfWeek

@Serializable
data class Time(
    val hour:Int,
    val minute:Int,
)

@Serializable
data class TimeSpan(
    val start: Time,
    val end: Time,
    val day: DayOfWeek
)

@Serializable
data class Subject(
    val title: String,
    val color: Int,
)


class SubjectsData : Data<Subject> {
    override fun read(context: Context): List<Subject> {
        var dir = ""
        runBlocking {
            dir = DataStoreState(context, DataStoreState.PATH).get("")
        }

        val file = File(context.filesDir.path + dir + "/subject.json")
        if (!file.exists()) {
            return emptyList()
        }

        val fileContent = file.readText()
        return Json.decodeFromString<List<Subject>>(fileContent)
    }
    override fun save(context: Context,list: List<Subject>) {
        var dir = ""
        runBlocking {
            dir = DataStoreState(context, DataStoreState.PATH).get("")
        }

        val file = File(context.filesDir.path + dir + "/subject.json")
        file.printWriter().use { out ->
            out.println(Json.encodeToString(list))
        }
    }
}