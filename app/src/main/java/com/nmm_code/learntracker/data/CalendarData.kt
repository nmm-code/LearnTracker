package com.nmm_code.learntracker.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert

//@Database(
//    entities = [Calendar::class, Date::class, Time::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun calendarDao(): CalendarDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
//
//
//@Dao
//interface CalendarDao {
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertCalendar(calendar: Calendar): Long
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTime(time:Time) : Long
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertDate(date: Date): Long
//
//    @Query("SELECT * FROM time WHERE minute=:minute AND hour=:hour ")
//    fun getTime(minute: Int, hour: Int): Long
//
//    @Transaction
//    fun insertCalendarWithTimeAndDate(
//        userId: Int,
//        startTime: Time,
//        endTime: Time,
//        startDate: Date,
//        endDate: Date
//    ): Long {
//        val startTimeId = insertTime(startTime)
//        val endTimeId = insertTime(endTime)
//
//        val startDateId = insertDate(startDate)
//        val endDateId = insertDate(endDate)
//
//        val calendarWithIds = Calendar(
//            userId,
//            startTime = startTimeId,
//            endTime = endTimeId,
//            startDay = startDateId,
//            endDay = endDateId
//        )
//
//        return insertCalendar(calendarWithIds)
//    }
//}
//
//@Entity
//data class CalendarWithDateTimeAndDate(
//    @Embedded val calendar: Calendar,
//
//    @Relation(
//        parentColumn = "startTime",
//        entityColumn = "id"
//    )
//    val startTime: Time,
//
//    @Relation(
//        parentColumn = "endTime",
//        entityColumn = "id"
//    )
//    val endTime: Time,
//
//    @Relation(
//        parentColumn = "startDay",
//        entityColumn = "id"
//    )
//    val startDay: Date,
//
//    @Relation(
//        parentColumn = "endDay",
//        entityColumn = "id"
//    )
//    val endDay: Date
//)
//
//
//@Entity
//data class Calendar(
//    val userId: Int,
//
//    val startTime: Long,
//    val endTime: Long,
//
//    val startDay: Long,
//    val endDay: Long,
//
//    @PrimaryKey(true) var id: Long = 0,
//)
//
//@Entity
//data class Date(
//    val day: Int,
//    val year: Int,
//
//    @PrimaryKey(true) var id: Long = 0,
//)
//
//@Entity
//data class Time(
//    val hour: Int,
//    val minute: Int,
//
//    @PrimaryKey(true) var id: Long = 0,
//)