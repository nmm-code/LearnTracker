package com.nmm_code.learntracker.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2H
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.abs

class CalendarActivity : ComponentActivity() {

    private fun offset2LocalDate(monthOffSet: MutableIntState) =
        if (monthOffSet.intValue < 0)
            LocalDate.now().minusMonths((monthOffSet.intValue * -1).toLong())
        else
            LocalDate.now().plusMonths(monthOffSet.intValue.toLong())

    private fun getMonthList(
        daysInMonth: Int,
        firstDayOfMonth: LocalDate
    ): MutableList<LocalDate?> {
        val dayList = getDaysOfWeek()

        val list =
            (0 until daysInMonth).map { firstDayOfMonth.plusDays(it.toLong()) }.toMutableList()

        val firstDay = firstDayOfMonth.dayOfWeek.value
        var finalDay = dayList[0].value
        while (firstDay != finalDay) {
            list.add(0, null)
            finalDay++
            if (finalDay == 8) {
                finalDay = 1
            }
        }
        var idx = 0
        val space = abs(list.size % 7 - 7)
        while (idx < space) {
            list.add(null)
            idx++
        }
        return list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                CalendarPage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CalendarPage(modifier: Modifier = Modifier) {
        val monthOffSet = remember { mutableIntStateOf(0) }
        val today = offset2LocalDate(monthOffSet)

        Scaffold(
            topBar = { TopBar(title = today.year.toString()) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        startActivity(Intent(this, AddCalendarEntryActivity::class.java))
                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = stringResource(R.string.add_a_new))
                }
            }
        ) {
            Column(
                Modifier
                    .padding(it)
                    .padding(horizontal = MaterialTheme.space.padding2)
            ) {
                CalendarHeader(monthOffSet, today)
                Calendar(monthOffSet)
                HorizontalDivider()
                Appointments(monthOffSet)
            }
        }
    }

    @Composable
    private fun CalendarHeader(offset: MutableIntState, today: LocalDate) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { offset.intValue-- }, Modifier.weight(1f)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = null)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(7f)
            ) {
                Headline1(
                    text = today.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ), textAlign = TextAlign.Center
                )

            }
            IconButton(onClick = { offset.intValue++ }, Modifier.weight(1f)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
            }
            IconButton(onClick = { offset.intValue = 0 }) {
                Icon(imageVector = Icons.Default.Today, contentDescription = null)
            }
        }
    }

    @Composable
    private fun CalendarWeekDays(adjustedDaysOfWeek: List<DayOfWeek>) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 5.dp)
        ) {
            adjustedDaysOfWeek.forEach {
                Paragraph2(
                    text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    @Composable
    private fun Calendar(offset: MutableIntState) {
        val today = offset2LocalDate(offset)

        val daysOfWeek = getDaysOfWeek()

        val firstDayOfMonth = today.withDayOfMonth(1)

        val list = getMonthList(today.lengthOfMonth(), firstDayOfMonth)

        CalendarWeekDays(daysOfWeek)

        list.chunked(7).forEach { chunk ->
            Row(Modifier.fillMaxWidth()) {
                chunk.forEach {
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(1.5f)
                    ) {
                        if (it == null)
                            Paragraph1(text = "")
                        else
                            Paragraph1(
                                text = it.dayOfMonth.toString(),
                                textAlign = TextAlign.Center,
                                color = if (LocalDate.now().dayOfYear == it.dayOfYear && LocalDate.now().year == it.year) Color.White else Color.Unspecified,
                                modifier = if (LocalDate.now().dayOfYear == it.dayOfYear && LocalDate.now().year == it.year) Modifier
                                    .align(Alignment.Center)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 5.dp, vertical = 7.dp) else Modifier
                                    .align(Alignment.Center)
                                    .aspectRatio(1f)
                                    .padding(horizontal = 5.dp, vertical = 7.dp)
                            )
                    }
                }
            }
        }
    }

    private fun getDaysOfWeek(): List<DayOfWeek> {
        val daysOfWeek = DayOfWeek.entries
        val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val adjustedDaysOfWeek =
            daysOfWeek.drop(firstDayOfWeek.ordinal) + daysOfWeek.take(firstDayOfWeek.ordinal)
        return adjustedDaysOfWeek
    }

    private data class Duration(
        val startH: Int,
        val startM: Int,
        val endH: Int,
        val endM: Int
    )

    @Composable
    private fun Appointments(offset: MutableIntState) {
        // TODO getOfOffset
        val list = remember {
            mutableStateListOf(

                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now(),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(1),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(2),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(3),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(4),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(5),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(6),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(7),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ), Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(8),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(9),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(10),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ), Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(11),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(12),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                    )
                ),
                Pair<LocalDate, List<Pair<Duration, String>>>(
                    LocalDate.now().plusDays(13),
                    listOf(
                        Pair(Duration(9, 30, 10, 0), "Mathe"),
                        Pair(Duration(10, 30, 11, 0), "Deutsch"),
                        Pair(Duration(12, 30, 14, 0), "Mathe")
                    )
                )
            )
        }
        LazyColumn {
            items(list, key = { it.first }) { item ->
                Row(Modifier.padding(vertical = 8.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Paragraph2H(
                            text = item.first.dayOfWeek.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        )
                        Headline1(
                            text = item.first.dayOfMonth.toString(),
                            fontWeight = FontWeight.W300
                        )
                    }

                    Column(Modifier.weight(8f)) {
                        item.second.forEach { entry ->
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp)
                            ) {
                                Paragraph2H(
                                    text = entry.second,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(start = 8.dp)
                                )
                                val timeRange = entry.first
                                Paragraph2(
                                    text = "%02d:%02d - %02d:%02d".format(
                                        timeRange.startH,
                                        timeRange.startM,
                                        timeRange.endH,
                                        timeRange.endM
                                    ),
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(start = 8.dp, top = 25.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}