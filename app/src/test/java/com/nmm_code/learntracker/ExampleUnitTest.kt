package com.nmm_code.learntracker

import com.nmm_code.learntracker.data.TimerActivity
import com.nmm_code.learntracker.data.TimerActivityData
import org.junit.Test

import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testMerge() {
        val local = LocalDate.now()
        local.dayOfYear -14
        var data = listOf(
        TimerActivity(0,10,local.dayOfYear -14,2024),
        TimerActivity(0,10,local.dayOfYear -13,2024),
        TimerActivity(0,10,local.dayOfYear -12,2024)
        )
         data = TimerActivityData.mergeLast2Weeks(
            data
        )
        println(data.size)
    }
}