package com.nmm_code.learntracker.pages

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.Subject
import com.nmm_code.learntracker.data.SubjectsData
import com.nmm_code.learntracker.data.TimerActivity
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.service.TimerService
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1H
import kotlinx.coroutines.delay
import java.time.LocalDate

private const val dialogWidth = 400

class TrackTimeActivity : ComponentActivity() {
    private val data = SubjectsData()

    private fun createNotificationChannel() {
        val name = "Timer Channel"
        val descriptionText = "Channel für Timer Service"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TIMER_CHANNEL", name, importance)
        channel.description = descriptionText
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                )

            }
        }
        createNotificationChannel()

        setContent {
            LearnTrackerTheme {
                TimerPage()
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun TimerPage(modifier: Modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopBar(title = stringResource(R.string.timer))
            ClockPage()
        }
    }

    @Composable
    fun ClockPage() {
        val local = LocalDate.now()
        val list =
            TimerActivityData.mergeLast2Weeks(TimerActivityData().read(this)).toMutableStateList()

        val activeIndex = remember {
            mutableIntStateOf(0)
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubjectDropDown(activeIndex)
            Clock(list, activeIndex)
            RecentActivity(list = list)
        }
    }

    private fun getTitleOfIndex(index: Int): Subject {
        return data.read(this)[index]
    }

    @Composable
    fun RecentActivity(modifier: Modifier = Modifier, list: SnapshotStateList<TimerActivity>) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
        ) {
            Headline2(
                text = stringResource(R.string.recent_activities),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            )
            LazyRow(Modifier.fillMaxWidth()) {
                item {
                    Spacer(
                        modifier = modifier
                            .size(MaterialTheme.space.padding6)
                            .padding(MaterialTheme.space.padding1)
                    )
                }
                items(list) {
                    val elem = getTitleOfIndex(it.id)
                    Surface(
                        color = Color(elem.color),
                        shape = RoundedCornerShape(MaterialTheme.space.padding2),
                        modifier = modifier
                            .height(150.dp)
                            .width(150.dp)
                            .padding(MaterialTheme.space.padding1),
                    ) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            Headline2(
                                text = elem.title,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.BottomStart)
                            )
                            Paragraph1H(
                                text = "${it.seconds / 3600}h ${it.seconds / 60}min ",
                                color = Color.White,
                            )
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = modifier
                            .size(MaterialTheme.space.padding6)
                            .padding(MaterialTheme.space.padding1)
                    )
                }
            }
        }
    }


    @Composable
    fun Clock(list: SnapshotStateList<TimerActivity>, activeIndex: MutableIntState) {
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        val running = sharedPreferences.getBoolean("running", false)
        val s = sharedPreferences.getInt("seconds", 0)

        var seconds by remember { mutableIntStateOf(s) }

        var isRunning by remember { mutableStateOf(running) }

        val startService: () -> Unit = {
            val intent = Intent(this, TimerService::class.java)
            intent.action = "START_TIMER"
            startForegroundService(intent)
            isRunning = true
        }

        val stopService: () -> Unit = {
            val intent = Intent(this, TimerService::class.java)
            stopService(intent)
            isRunning = false
        }

        fun resetTimer() {
            with(sharedPreferences.edit()) {
                putInt("seconds", 0).apply()
                putBoolean("running", false).apply()
            }
            seconds = 0
            isRunning = false
        }
        LaunchedEffect(isRunning) {
            while (isRunning) {
                seconds = sharedPreferences.getInt("seconds", seconds)
                delay(1000L)
            }
        }

        Box(
            modifier = Modifier
                .padding(50.dp)
                .size(250.dp)
        ) {
            val color = MaterialTheme.colorScheme.primary
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                drawCircle(color, style = Stroke(20f))
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(85.dp)
            ) {

                Headline1(
                    text = "%02d".format(seconds / 60),
                    size = 45.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
                Headline1(
                    text = "%02d".format(seconds % 60),
                    size = 35.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            val dp by animateDpAsState(
                if (!isRunning) 30.dp else 50.dp, label = "", animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
            FloatingActionButton(
                onClick = {
                    isRunning = false
                    stopService()
                    resetTimer()
                },

                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            FloatingActionButton(
                onClick = {
                    isRunning = !isRunning
                    if (isRunning) {
                        startService()
                    } else {
                        stopService()
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier

            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null, tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(dp)
                )
            }
            FloatingActionButton(
                onClick = {
                    val date = LocalDate.now()
                    list.add(
                        TimerActivity(
                            activeIndex.intValue,
                            seconds.toLong(),
                            date.dayOfYear,
                            date.year
                        )
                    )
                    TimerActivityData().save(this@TrackTimeActivity, list)

                    val mergeList = TimerActivityData.mergeLast2Weeks(list)
                    list.clear()
                    list.addAll(mergeList)

                    isRunning = false
                    stopService()
                    resetTimer()
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }


    @Composable
    fun SubjectDropDown(activeIndex: MutableIntState) {
        val entries = SubjectsData().read(this)

        var isSelecting by remember {
            mutableStateOf(false)
        }

        if (entries.isNotEmpty())
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = MaterialTheme.space.padding5)
            ) {
                val it = entries[activeIndex.intValue]
                Canvas(
                    modifier = Modifier
                        .size(MaterialTheme.space.padding5)
                ) {
                    drawCircle(color = Color(it.color), style = Stroke(16f))
                }
                Headline1(
                    modifier = Modifier.padding(MaterialTheme.space.padding2),
                    text = it.title,
                    size = 30.sp
                )

                IconButton(
                    onClick = { isSelecting = !isSelecting },
                    Modifier.size(MaterialTheme.space.padding4)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChangeHistory,
                        contentDescription = "drop down icon",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        if (isSelecting)
            SubjectDialog(
                entries,
                { isSelecting = false },
                activeIndex.intValue,
                { idx -> activeIndex.intValue = idx }
            )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SubjectDialog(
        entries: List<Subject>,
        onDismissRequest: () -> Unit = { },
        activeIdx: Int,
        onSelect: (Int) -> Unit
    ) {
        BasicAlertDialog(onDismissRequest = { onDismissRequest() }) {
            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(MaterialTheme.space.padding2)
                    )
                    .background(MaterialTheme.colorScheme.background)
            ) {
                entries.forEachIndexed { index, entry ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.space.padding3,
                                vertical = MaterialTheme.space.padding2
                            )
                            .width(dialogWidth.dp)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    onSelect(index)
                                    onDismissRequest()
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.size(MaterialTheme.space.padding5)) {
                            drawCircle(
                                color = Color(entry.color),
                                style = Stroke(16f),
                            )
                        }
                        Headline1(
                            text = entry.title,
                            fontWeight = (if (index == activeIdx) FontWeight.Bold else FontWeight.Normal),
                            modifier = Modifier.padding(start = MaterialTheme.space.padding4)
                        )
                    }
                }
            }

        }
    }


}