package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.serialization.Serializable


@Serializable
data class CalendarEntry(
    val idName: Int,
    val idDuration: Int,
    val idDate: Int,
)

// LOCAL

object CalendarData : Data<CalendarEntry>() {
    override val fileName: String = "/calendar.bin"
    override fun getList(context: Context): List<CalendarEntry> = super.read<CalendarEntry>(context)
    override fun saveList(context: Context, list: List<CalendarEntry>) = super.save<CalendarEntry>(context, list)
}

object NameData : Data<String>() {
    override val fileName: String = "/names.bin"
    override fun getList(context: Context): List<String> = super.read<String>(context)
    override fun saveList(context: Context, list: List<String>) = super.save<String>(context, list)
}


// GLOBAL

@Serializable
data class DateEntry(
    val idDayOfYear: Int,
    val idYear: Int,
)

object DateDataGlobal : Data<DateEntry>(false) {
    override val fileName: String = "/dates.bin"
    override fun getList(context: Context): List<DateEntry> = super.read<DateEntry>(context)
    override fun saveList(context: Context, list: List<DateEntry>) = super.save<DateEntry>(context, list)
}

object DayOfYearDataGlobal : Data<Int>(false) {
    override val fileName: String = "/days-of-year.bin"
    override fun getList(context: Context): List<Int> = super.read<Int>(context)
    override fun saveList(context: Context, list: List<Int>) = super.save<Int>(context, list)
}

object YearDataGlobal : Data<Int>(false) {
    override val fileName: String = "/years.bin"
    override fun getList(context: Context): List<Int> = super.read<Int>(context)
    override fun saveList(context: Context, list: List<Int>) = super.save<Int>(context, list)
}


@Serializable
data class DurationEntry(
    val idStart:Int,
    val idEnd:Int,
)

object DurationDataGlobal : Data<DurationEntry>(false) {
    override val fileName: String = "/durations.bin"
    override fun getList(context: Context): List<DurationEntry> = super.read<DurationEntry>(context)
    override fun saveList(context: Context, list: List<DurationEntry>) = super.save<DurationEntry>(context, list)
}

@Serializable
data class Time(
    val hour:Int,
    val minute:Int,
)
object TimeDataGlobal : Data<Time>(false) {
    override val fileName: String = "/times.bin"
    override fun getList(context: Context): List<Time> = super.read<Time>(context)
    override fun saveList(context: Context, list: List<Time>) = super.save<Time>(context, list)
}