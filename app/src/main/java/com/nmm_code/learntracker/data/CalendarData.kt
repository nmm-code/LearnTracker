package com.nmm_code.learntracker.data

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CalendarEntry(
    val year: Int,
    val dayOfYear: Int
)

object CalendarData : Data<CalendarEntry>() {
    override val fileName: String = "/calendar.bin"
}