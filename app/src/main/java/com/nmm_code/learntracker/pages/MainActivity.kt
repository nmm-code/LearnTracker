package com.nmm_code.learntracker.pages

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.data.Subject
import com.nmm_code.learntracker.data.SubjectsData
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.pre.WorkingTitleActivity
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class NavigationElements(
    val title: Int,
    val id: Int,
    val color: Color = Color.Black,
    val activity: Class<*>
)

private const val BOX_WIDTH = 160

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            DataStoreState(this@MainActivity, DataStoreState.PAGE).set(3)
            val path = DataStoreState(this@MainActivity, DataStoreState.PATH).get("")
            val success = File(application.filesDir.path + path).mkdirs()
            println(success)
        }
        setContent {
            LearnTrackerTheme {
                MainPage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainPage(modifier: Modifier = Modifier) {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            topBar = {
                TopBar(title = "Learn Tracker", onClick = {
                    lifecycleScope.launch {
                        val string =
                            DataStoreState(this@MainActivity, DataStoreState.PATH).get("")
                                .split("/")
                        val mergedPath = string.dropLast(1).joinToString("/")
                        startActivity(Intent(this@MainActivity, WorkingTitleActivity::class.java))
                        finish()

                        DataStoreState(
                            this@MainActivity,
                            DataStoreState.PATH
                        ).set(mergedPath)
                    }
                })
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            NavigationGrid(
                Modifier
                    .padding(it)
                    .padding(horizontal = MaterialTheme.space.padding2),
                snackbarHostState
            )
        }
    }

    @Composable
    private fun NavigationGrid(
        modifier: Modifier = Modifier,
        snackBar: SnackbarHostState
    ) {
        val navigationElements = listOf(
            NavigationElements(
                R.string.timer,
                R.drawable.ic_timer,
                Color(83, 109, 198, 255),
                activity = TrackTimeActivity::class.java
            ),
            NavigationElements(
                R.string.schedule,
                R.drawable.ic_schedule,
                color = Color(78, 185, 75, 255),
                activity = ScheduleActivity::class.java
            ),
            NavigationElements(
                R.string.calendar,
                R.drawable.ic_calendar,
                color = Color(194, 97, 184, 255),
                activity = CalendarActivity::class.java
            ),
            NavigationElements(
                R.string.subjects,
                R.drawable.ic_subjects,
                color = Color(208, 72, 72, 255),
                activity = SubjectsActivity::class.java
            ),
            NavigationElements(
                R.string.dashboard,
                R.drawable.ic_dashboard,
                color = Color(103, 202, 202, 255),
                activity = DashboardActivity::class.java
            ),
            NavigationElements(
                R.string.tasks,
                R.drawable.ic_tasks,
                color = Color(204, 220, 14, 255),
                activity = TasksActivity::class.java
            ),
            NavigationElements(
                R.string.feedback,
                R.drawable.ic_send,
                color = Color(6, 2, 22, 255),
                activity = MainActivity::class.java
            ),
        )

        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = object : StaggeredGridCells {
                override fun Density.calculateCrossAxisCellSizes(
                    availableSize: Int,
                    spacing: Int
                ): IntArray {
                    val firstColumn = (availableSize - spacing) / 2
                    val secondColumn = availableSize - spacing - firstColumn
                    return intArrayOf(firstColumn, secondColumn)
                }

            },

            ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.space.padding5)
                )
            }
            navigationElements.forEachIndexed { idx, item ->

                item(span =  StaggeredGridItemSpan.SingleLane) {
                    BoxElement(item, idx, snackBar)
                }

            }
        }
    }

    @Composable
    fun BoxElement(elem: NavigationElements, idx: Int, snackbarHostState: SnackbarHostState) {
        val color = getAccessibleTextColor(elem.color)

        val modifier = if (idx != 1 && idx != 3 && idx != 5) Modifier
            .size(BOX_WIDTH.dp) else
            Modifier
                .height((BOX_WIDTH * 2).dp)
                .width(BOX_WIDTH.dp)

        Surface(
            color = elem.color,
            onClick = {
                if (getString(elem.title) == "Feedback") {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                } else {
                    when (elem.id) {
                        R.drawable.ic_timer -> {
                            val list =
                                SubjectsData.read<Subject>(this)

                            if (list.isNotEmpty())
                                startActivity(Intent(this@MainActivity, elem.activity))
                            else {
                                lifecycleScope.launch {
                                    snackbarHostState.showSnackbar(getString(R.string.you_need_to_create_the_subjects_before_tracking))
                                }
                            }
                        }

                        else -> {
                            startActivity(Intent(this@MainActivity, elem.activity))
                        }
                    }
                }
            },
            shape = RoundedCornerShape(MaterialTheme.space.padding2),
            modifier = modifier.padding(MaterialTheme.space.padding1)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = MaterialTheme.space.padding2,
                        vertical = MaterialTheme.space.padding1
                    )
            ) {
                Icon(
                    painterResource(id = elem.id),
                    "Icon of the Box",
                    Modifier
                        .align(Alignment.TopEnd)
                        .size((BOX_WIDTH / 2.5).dp),
                    tint = color
                )

                Headline2(
                    text = stringResource(elem.title),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = MaterialTheme.space.padding3),
                    color = color,
                )
                Paragraph2(
                    text = getTextOfElem(elem),
                    color = color,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = MaterialTheme.space.padding0)
                )
            }
        }
    }

    private fun getTextOfElem(elem: NavigationElements): String {
        return when (elem.id) {
            R.drawable.ic_calendar -> {
                val today = LocalDate.now()
                val locale = Locale.getDefault()
                val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                val month = today.month.getDisplayName(TextStyle.SHORT, locale)

                "${today.dayOfMonth}. $month"
            }

            R.drawable.ic_timer -> {
                val list = TimerActivityData.mergeLast2Weeks(TimerActivityData().read(this))
                val minute = list.sumOf { it.seconds } / 60
                val hour = list.sumOf { it.seconds } / 3600
                if (minute.toInt() == 0) "" else getString(R.string.recent_tracked) + ": %02d:%02d".format(
                    hour,
                    minute
                )
            }

            else -> ""
        }
    }
}
