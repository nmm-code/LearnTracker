package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.abs

class CalendarActivity : ComponentActivity() {
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
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.calendar))
            }
        ) {
            Column(
                Modifier
                    .padding(it)
                    .padding(
                        vertical = MaterialTheme.space.padding4,
                        horizontal = MaterialTheme.space.padding3
                    )
            ) {
                Header()
                Calendar()
                Appointments()
            }
        }
    }

    @Composable
    fun Header(modifier: Modifier = Modifier) {
        val today = LocalDate.now()
        Headline1(text = today.month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
    }

    @Composable
    fun Calendar(modifier: Modifier = Modifier) {

        val defaultLocale: Locale = Locale.getDefault()

        val firstDayOfWeek: DayOfWeek = WeekFields.of(defaultLocale).firstDayOfWeek

        val daysOfWeek = DayOfWeek.entries

        val adjustedDaysOfWeek =
            daysOfWeek.drop(firstDayOfWeek.ordinal) + daysOfWeek.take(firstDayOfWeek.ordinal)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 5.dp)
        ) {
            adjustedDaysOfWeek.forEach {
                Paragraph1(
                    text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        val today = LocalDate.now().plusWeeks(23)
        val daysInMonth = today.lengthOfMonth()
        val firstDayOfMonth = today.withDayOfMonth(1)


        val list =
            (0 until daysInMonth).map { firstDayOfMonth.plusDays(it.toLong()) }.toMutableList()

        var idx = 0
        var space = firstDayOfMonth.dayOfWeek.value % 7
        while (idx < space) {
            list.add(0, null)
            idx++
        }
        idx = 0
        space = abs(list.size % 7 - 7)
        while (idx < space) {
            list.add(null)
            idx++
        }

        list.chunked(7).forEachIndexed { index, chunk ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                chunk.forEach {
                    if (it == null) {
                        println("LJKLFKSDJLKJDJF")
                        Paragraph1(text = "", modifier = Modifier.weight(1f))
                    } else
                        Paragraph1(
                            text = it.dayOfMonth.toString(),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                }
            }
        }
    }

    @Composable
    fun Appointments(modifier: Modifier = Modifier) {

    }
}