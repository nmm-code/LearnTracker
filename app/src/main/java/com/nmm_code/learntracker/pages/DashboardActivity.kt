package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.PieChartWithLegend
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.TimerActivity
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.logic.TimeUtils
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1H
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import java.time.LocalDate

class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                DashboardPage()
            }
        }
    }

    enum class Filter(var strCode: Int) {
        LAST_WEEK(R.string.last_week),
        LAST_MONTH(R.string.last_month),
        LAST_YEAR(R.string.last_year);

        fun next(): Filter {
            return when (this) {
                LAST_WEEK -> LAST_MONTH
                LAST_MONTH -> LAST_YEAR
                LAST_YEAR -> LAST_WEEK
            }
        }
    }

    private fun getStreak(list: List<TimerActivity>): Int {
        if (list.isEmpty()) return 0

        val sortedList = list.sortedWith(compareBy({ it.year }, { it.day }))

        var longestStreak = 1
        var currentStreak = 1

        for (i in 1 until sortedList.size) {
            val currentActivity = sortedList[i]
            val previousActivity = sortedList[i - 1]

            if (currentActivity.year == previousActivity.year && currentActivity.day == previousActivity.day + 1 ||
                (currentActivity.year == previousActivity.year + 1 && currentActivity.day == 1 && previousActivity.day == 365)
            ) {
                currentStreak++
            } else {
                longestStreak = maxOf(longestStreak, currentStreak)
                currentStreak = 1
            }
        }

        return maxOf(longestStreak, currentStreak)
    }


    @Preview(showBackground = true)
    @Composable
    fun DashboardPage(modifier: Modifier = Modifier) {
        var state by remember {
            mutableStateOf(Filter.LAST_MONTH)
        }

        Scaffold(
            topBar = {
                TopBar(title = getString(state.strCode), actions = {
                    state = state.next()
                })
            },
            modifier = Modifier
        ) {
            val list = getList(state)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
            ) {
                item {
                    Column(
                        Modifier.padding(horizontal = MaterialTheme.space.padding4)
                    ) {
                        Diagram(list)
                        Stats(list)
                        Headline1(
                            text = stringResource(R.string.history),
                            modifier = Modifier.padding(vertical = 50.dp)
                        )
                    }
                }
                item {
                    History(list)
                }
            }
        }
    }

    @Composable
    private fun Diagram(list: List<TimerActivity>) {
        val mergedList = TimerActivityData.merge(list)
        val data = mergedList.map { it.seconds.toFloat() }
        val dataStore = TimerActivityData
        val names = mergedList.map { dataStore.getTitleOfIndex(this, it.id).title }
        val colors = mergedList.map { Color(dataStore.getTitleOfIndex(this, it.id).color) }
        Box(modifier = Modifier.fillMaxSize()) {
            PieChartWithLegend(
                data,
                colors,
                names,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    private fun History(list: List<TimerActivity>) {
        val mergedList = TimerActivityData.merge(list)
        LazyRow {
            items(mergedList) { item ->
                val l = TimerActivityData.getTitleOfIndex(
                    this@DashboardActivity,
                    item.id
                )
                val color = Color(l.color)
                Box(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .size(200.dp)
                        .clip(RoundedCornerShape(16))
                        .background(Color(l.color))
                        .padding(20.dp)
                ) {
                    Headline2(
                        text = TimeUtils.formatSeconds(item.seconds),
                        color = getAccessibleTextColor(color),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                    Column(Modifier.align(Alignment.BottomStart)) {
                        Paragraph1H(
                            text = l.title,
                            color = getAccessibleTextColor(color),
                            softWrap = true
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Paragraph2(
                            text = getString(R.string.total_time),
                            color = getAccessibleTextColor(color)
                        )
                    }
                }

            }
        }
    }

    @Composable
    private fun Stats(list: List<TimerActivity>) {

        Row(modifier = Modifier.fillMaxWidth()) {
            val color = MaterialTheme.colorScheme.primary
            val second = MaterialTheme.colorScheme.error

            Surface(
                shape = RoundedCornerShape(MaterialTheme.space.padding2),
                color = color,
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Paragraph1(
                        text = stringResource(R.string.time_spend),
                        softWrap = true,
                        color = getAccessibleTextColor(color)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Paragraph1H(
                        text = list.sumOf { item -> item.seconds / 3600 }.toString() + " h",
                        color = getAccessibleTextColor(color)
                    )
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Surface(
                shape = RoundedCornerShape(MaterialTheme.space.padding2),
                color = second,
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Paragraph1(
                        text = stringResource(R.string.longest) + " Streak",
                        color = getAccessibleTextColor(second)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Paragraph1H(
                        text = getStreak(list).toString() + " " + stringResource(R.string.days),
                        color = getAccessibleTextColor(second)
                    )
                }
            }
        }
    }

    @Composable
    private fun getList(state: Filter): List<TimerActivity> {
        val list = TimerActivityData.getList(this)
        val local = LocalDate.now()
        return when (state) {
            Filter.LAST_YEAR -> {
                val oneYearAgo = local.minusYears(1)
                list.filter {
                    val activityDate = LocalDate.of(it.year, 1, 1).plusDays(it.day.toLong() - 1)
                    activityDate.isAfter(oneYearAgo) && activityDate.isBefore(local.plusDays(1))
                }
            }

            Filter.LAST_WEEK -> {
                val oneWeekAgo = local.minusWeeks(1)
                list.filter {
                    val activityDate = LocalDate.of(it.year, 1, 1).plusDays(it.day.toLong() - 1)
                    activityDate.isAfter(oneWeekAgo) && activityDate.isBefore(local.plusDays(1))
                }
            }

            Filter.LAST_MONTH -> {
                val oneMonthAgo = local.minusMonths(1)
                list.filter {
                    val activityDate = LocalDate.of(it.year, 1, 1).plusDays(it.day.toLong() - 1)
                    activityDate.isAfter(oneMonthAgo) && activityDate.isBefore(local.plusDays(1))
                }
            }
        }
    }
}