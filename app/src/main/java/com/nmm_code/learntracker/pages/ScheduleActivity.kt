package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1H
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2H
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar

class ScheduleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                SchedulePage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SchedulePage(modifier: Modifier = Modifier) {
        Scaffold(
            topBar = {
                TopBar(title = "Schedule")
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
                HorizontalDivider(Modifier.padding(bottom = 20.dp))
                WeekDays()
                HorizontalDivider(Modifier.padding(top = 20.dp))
                ScheduleField()
            }
        }
    }

    @Composable
    fun ScheduleField(modifier: Modifier = Modifier) {
        val list = (0..23).toMutableList()
        list.add(0)
        LazyColumn {
            items(list) {
                val time = LocalTime.of(it, 0)
                val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, java.util.Locale.getDefault())
                val pattern = if (dateFormat.format(Calendar.getInstance().time).contains("AM") || dateFormat.format(Calendar.getInstance().time).contains("PM")) {
                    "hh a"
                } else {
                    "HH:mm"
                }
                val formatter = DateTimeFormatter.ofPattern(pattern, java.util.Locale.getDefault())

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Paragraph2(
                        text = time.format(formatter).lowercase(),
                        modifier = Modifier.weight(2f),
                    )
                    HorizontalDivider(modifier = Modifier.weight(8f))
                }
            }
        }
    }


    @Composable
    private fun Header() {
        val current = LocalDateTime.now()

        val month = current.month.getDisplayName(TextStyle.FULL, java.util.Locale.getDefault())
        val weekday =
            current.dayOfWeek.getDisplayName(TextStyle.FULL, java.util.Locale.getDefault())
        val year = current.year

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Headline1(text = month.toString())
                Spacer(Modifier.padding(5.dp))
                Paragraph1H(text = weekday)
                Spacer(Modifier.padding(20.dp))
            }
            val color = MaterialTheme.colorScheme.onBackground
            Headline1(
                text = year.toString(),
                size = 50.sp,
                color = Color(color.red, color.green, color.blue, 0.1f)
            )
        }
    }

    @Composable
    fun WeekDays(modifier: Modifier = Modifier) {
        val current = LocalDateTime.now()
        val list = listOf(
            current,
            current.plusDays(1),
            current.plusDays(2),
            current.plusDays(3),
            current.plusDays(4),
            current.plusDays(5),
            current.plusDays(6)
        )
        Row(horizontalArrangement = Arrangement.Start) {
            list.forEach {
                WeekDay(
                    it,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }

    @Composable
    fun WeekDay(localDateTime: LocalDateTime, modifier: Modifier = Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Paragraph1(
                text = localDateTime.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    java.util.Locale.getDefault()
                ).substring(0, 3),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(5.dp))

            Paragraph2H(
                text = localDateTime.dayOfMonth.toString(),
                color = if (localDateTime.dayOfMonth == LocalDate.now().dayOfMonth) Color.White else Color.Unspecified,
                modifier = if (localDateTime.dayOfMonth == LocalDate.now().dayOfMonth) Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
                else Modifier
                    .clip(CircleShape)
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
    
}