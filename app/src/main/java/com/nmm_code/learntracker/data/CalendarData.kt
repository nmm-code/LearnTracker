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
    override var list: List<CalendarEntry>? = null

    override fun getList(context: Context): List<CalendarEntry> {
        if (list == null)
            list = super.read(context)

        return list as List<CalendarEntry>
    }

    override fun saveList(context: Context, list: List<CalendarEntry>) {
        this.list = list

        super.save(context, list)
    }
}

object NameData : Data<String>() {
    override val fileName: String = "/names.bin"
    override var list: List<String>? = null

    override fun getList(context: Context): List<String> {
        if (list == null)
            list = super.read(context)

        return list as List<String>
    }

    override fun saveList(context: Context, list: List<String>) {
        this.list = list

        super.save(context, list)
    }
}


// GLOBAL

@Serializable
data class DateEntry(
    val idDayOfYear: Int,
    val idYear: Int,
)

object DateDataGlobal : Data<DateEntry>(false) {
    override val fileName: String = "/dates.bin"
    override var list: List<DateEntry>? = null

    override fun getList(context: Context): List<DateEntry> {
        if (list == null)
            list = super.read(context)

        return list as List<DateEntry>
    }

    override fun saveList(context: Context, list: List<DateEntry>) {
        this.list = list

        super.save(context, list)
    }
}

object DayOfYearDataGlobal : Data<Int>(false) {
    override val fileName: String = "/days-of-year.bin"
    override var list: List<Int>? = null

    override fun getList(context: Context): List<Int> {
        if (list == null)
            list = super.read(context)

        return list as List<Int>
    }

    override fun saveList(context: Context, list: List<Int>) {
        this.list = list

        super.save(context, list)
    }
}

object YearDataGlobal : Data<Int>(false) {
    override val fileName: String = "/years.bin"
    override var list: List<Int>? = null

    override fun getList(context: Context): List<Int> {
        if (list == null)
            list = super.read(context)

        return list as List<Int>
    }

    override fun saveList(context: Context, list: List<Int>) {
        this.list = list

        super.save(context, list)
    }
}


@Serializable
data class DurationEntry(
    val idStart:Int,
    val idEnd:Int,
)

object DurationDataGlobal : Data<DurationEntry>(false) {
    override val fileName: String = "/durations.bin"
    override var list: List<DurationEntry>? = null

    override fun getList(context: Context): List<DurationEntry> {
        if (list == null)
            list = super.read(context)

        return list as List<DurationEntry>
    }

    override fun saveList(context: Context, list: List<DurationEntry>) {
        this.list = list

        super.save(context, list)
    }
}

@Serializable
data class Time(
    val hour:Int,
    val minute:Int,
)
object TimeDataGlobal : Data<Time>(false) {
    override val fileName: String = "/times.bin"
    override var list: List<Time>? = null

    override fun getList(context: Context): List<Time> {
        if (list == null)
            list = super.read(context)

        return list as List<Time>
    }

    override fun saveList(context: Context, list: List<Time>) {
        this.list = list

        super.save(context, list)
    }
}