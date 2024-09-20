package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.DayOfWeek

@Serializable
data class Time(
    val hour: Int,
    val minute: Int,
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


object SubjectsData : Data<Subject>() {
    override val fileName: String = "/subject.bin"
}