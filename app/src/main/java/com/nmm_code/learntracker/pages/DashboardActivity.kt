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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.PieChartWithLegend
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.TimerActivity
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
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

    @Preview
    @Composable
    private fun ff() {
        LearnTrackerTheme {
            DashboardPage()
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
        ) { it ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = MaterialTheme.space.padding4)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                val list = getList(state)
                Diagram(list)
                Stats(list)
                Headline1(text = "History", modifier = Modifier.padding(vertical = 50.dp))
                History(list)
            }
        }
    }

    @Composable
    private fun Diagram(list: List<TimerActivity>) {
        val list = TimerActivityData.merge(list)

        val data = list.map { it.seconds.toFloat() }

        val dataStore = TimerActivityData
        val names = list.map { dataStore.getTitleOfIndex(this, it.id).title }
        val colors = list.map { Color(dataStore.getTitleOfIndex(this, it.id).color) }
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
                        .height(200.dp)
                        .width(140.dp)
                        .clip(RoundedCornerShape(16))
                        .background(Color(l.color))
                        .padding(20.dp)
                ) {
                    Headline1(
                        text = (item.seconds / 3600).toString() + "h",
                        color = getAccessibleTextColor(color),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                    Column(Modifier.align(Alignment.BottomStart)) {

                        Headline1(text = l.title, color = getAccessibleTextColor(color))
                        Paragraph2(
                            text = "Total Time",
                            color = getAccessibleTextColor(color)
                        )
                    }
                }

            }
        }
    }

    @Composable
    private fun Stats(list: List<TimerActivity>) {

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            val color = MaterialTheme.colorScheme.primary
            val second = MaterialTheme.colorScheme.error

            Surface(
                shape = RoundedCornerShape(MaterialTheme.space.padding2),
                color = color,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Paragraph1(text = "Time spend", color = getAccessibleTextColor(color))
                    Spacer(modifier = Modifier.size(10.dp))
                    Paragraph1H(
                        text = list.sumOf { item -> item.seconds / 3600 }.toString() + " h",
                        color = getAccessibleTextColor(color)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(MaterialTheme.space.padding2),
                color = second
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Paragraph1(text = "Longest Streak", color = getAccessibleTextColor(second))
                    Spacer(modifier = Modifier.size(10.dp))
                    Paragraph1H(
                        text = getStreak(list).toString() + " Days",
                        color = getAccessibleTextColor(second)
                    )
                }
            }
        }
    }

    @Composable
    private fun getList(state: Filter): List<TimerActivity> {
        val list = TimerActivityData.read<TimerActivity>(this)
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