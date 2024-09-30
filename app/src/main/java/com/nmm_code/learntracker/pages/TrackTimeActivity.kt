package com.nmm_code.learntracker.pages

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.data.Subject
import com.nmm_code.learntracker.data.SubjectsData
import com.nmm_code.learntracker.data.TimerActivity
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.logic.TimeUtils
import com.nmm_code.learntracker.service.TimerService
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1H
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Timer

private const val dialogWidth = 400

class TrackTimeActivity : ComponentActivity() {
    private val data = SubjectsData

    private fun createNotificationChannel() {
        val name = "Timer Channel"
        val descriptionText = "Channel für Timer Service"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TIMER_CHANNEL", name, importance)
        channel.description = descriptionText
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
                TimerPageWithPermission()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun TimerPageWithPermission(modifier: Modifier = Modifier) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionState =
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            if (notificationPermissionState.status.isGranted) {
                TimerPage(modifier = modifier, true)
            } else {
                TimerPage(modifier = modifier, false)
                LaunchedEffect(Unit) {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        } else {
            TimerPage(modifier = modifier, true)
        }
    }

    @Composable
    fun TimerPage(modifier: Modifier = Modifier, isGranted: Boolean) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopBar(title = stringResource(R.string.timer))
            ClockPage(isGranted)
        }
    }

    @Composable
    fun ClockPage(isGranted: Boolean) {
        val list =
            TimerActivityData.mergeLast2Weeks(TimerActivityData.getList(this))
                .toMutableStateList()

        val activeIndex = remember {
            mutableIntStateOf(0)
        }

        SubjectDropDown(activeIndex)
        Clock(list, activeIndex, isGranted)
        RecentActivity(list = list)
    }

    private fun getTitleOfIndex(index: Int): Subject {
        return data.getList(this)[index]
    }

    @Composable
    fun RecentActivity(modifier: Modifier = Modifier, list: SnapshotStateList<TimerActivity>) {
        HorizontalDivider()
        LazyColumn(Modifier.fillMaxWidth()) {
            item {
                Headline1(
                    size = 20.sp,
                    text = stringResource(R.string.recent_activities),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                )
            }
            items(list) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    val elem = getTitleOfIndex(it.id)
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .size(30.dp)
                    ) {
                        drawCircle(Color(elem.color))
                    }
                    Paragraph1(text = elem.title, modifier = Modifier.weight(5f))
                    Paragraph1(
                        text = TimeUtils.formatSeconds(it.seconds),
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }


    @Composable
    fun Clock(
        list: SnapshotStateList<TimerActivity>,
        activeIndex: MutableIntState,
        isGranted: Boolean
    ) {
        val timeSinceData = DataStoreState(this, DataStoreState.TIME_SINCE)
        val secondsData = DataStoreState(this, DataStoreState.SECONDS)
        val lifecycle = rememberCoroutineScope()
        var isPaused by remember { mutableStateOf(true) }

        var savedSeconds by remember { mutableLongStateOf(0) }
        var timeSince by remember { mutableLongStateOf(0L) }

        var displayedSeconds by remember { mutableLongStateOf(0) }

        LaunchedEffect(Unit) {
            timeSince = timeSinceData.get(0)
            isPaused = timeSince.toInt() == 0
            savedSeconds = secondsData.get(0)
            displayedSeconds = if (isPaused)
                savedSeconds
            else
                savedSeconds + ((SystemClock.elapsedRealtime() - timeSince) / 1000)
        }

        LaunchedEffect(isPaused) {
            while (!isPaused) {
                delay(1000)
                displayedSeconds =
                    (savedSeconds + ((SystemClock.elapsedRealtime() - timeSince) / 1000))
            }
        }
        val startService: () -> Unit = {
            if (isGranted) {
                val intent = Intent(this, TimerService::class.java)
                intent.action = "START_TIMER"
                startForegroundService(intent)
            }
        }

        val stopService: () -> Unit = {
            if (isGranted) {
                val intent = Intent(this, TimerService::class.java)
                stopService(intent)
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val totalSeconds = displayedSeconds
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            if (hours != 0L) {
                Headline1(
                    text = "%02d:".format(hours),
                    size = 55.sp,
                )
            }
            Headline1(
                text = "%02d:%02d".format(minutes, seconds),
                size = 55.sp,
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
        ) {

            val dp by animateDpAsState(
                if (isPaused) 25.dp else 40.dp, label = "", animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
            FloatingActionButton(
                onClick = {
                    timeSince = 0
                    savedSeconds = 0
                    displayedSeconds = 0
                    lifecycle.launch {
                        timeSinceData.set(timeSince)
                        secondsData.set(savedSeconds)
                    }
                    isPaused = true
                    stopService()
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
                    if (isPaused) {
                        startService()
                        timeSince = SystemClock.elapsedRealtime()
                        lifecycle.launch {
                            timeSinceData.set(timeSince)
                        }
                    } else {
                        savedSeconds += ((SystemClock.elapsedRealtime() - timeSince) / 1000)
                        timeSince = 0
                        lifecycle.launch {
                            timeSinceData.set(timeSince)
                            secondsData.set(savedSeconds)
                        }
                        stopService()
                    }
                    isPaused = !isPaused
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier

            ) {
                Icon(
                    imageVector = if (!isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
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
                            displayedSeconds,
                            date.dayOfYear,
                            date.year
                        )
                    )
                    TimerActivityData.saveList(this@TrackTimeActivity, list)

                    val mergeList = TimerActivityData.mergeLast2Weeks(list)
                    list.clear()
                    list.addAll(mergeList)
                    timeSince = 0
                    savedSeconds = 0
                    displayedSeconds = 0
                    lifecycle.launch {
                        timeSinceData.set(timeSince)
                        secondsData.set(savedSeconds)
                    }
                    isPaused = true
                    stopService()
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

    @Preview
    @Composable
    fun SubjectDropDown(activeIndex: MutableIntState = mutableIntStateOf(0)) {
        val entries = SubjectsData.getList(this)

        var isSelecting by remember {
            mutableStateOf(false)
        }

        if (entries.isNotEmpty())
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    vertical = MaterialTheme.space.padding5,
                    horizontal = MaterialTheme.space.padding6
                )
            ) {
                val it = entries[activeIndex.intValue]
                Canvas(
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle(color = Color(it.color))
                }
                Spacer(Modifier.size(20.dp))
                Headline1(
                    text = it.title,
                    size = 20.sp,
                    wrap = true,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(8f)
                )
                Spacer(Modifier.size(20.dp))
                IconButton(
                    onClick = { isSelecting = !isSelecting },
                    Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
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
                                horizontal = MaterialTheme.space.padding2,
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
                        Canvas(modifier = Modifier.size(MaterialTheme.space.padding3)) {
                            drawCircle(
                                color = Color(entry.color),
                                style = if (index == activeIdx) Fill else Stroke(8f)
                            )
                        }
                        Paragraph1(
                            text = entry.title,
                            fontWeight = (if (index == activeIdx) FontWeight.Bold else FontWeight.Normal),
                            modifier = Modifier.padding(start = MaterialTheme.space.padding2)
                        )
                    }
                }
            }

        }
    }


}