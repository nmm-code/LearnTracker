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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.ColorDropDown
import com.nmm_code.learntracker.composable.IconRow
import com.nmm_code.learntracker.composable.IconRowTexField
import com.nmm_code.learntracker.composable.IntervalDropDown
import com.nmm_code.learntracker.composable.OPTION_INTERVAL
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AddCalendarEntryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearnTrackerTheme {
                Page()
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        LearnTrackerTheme {
            Page()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Page() {
        val timePickerState = rememberTimePickerState()
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = Calendar.getInstance().timeInMillis)

        val timePickerState2 = rememberTimePickerState()
        val datePickerState2 =
            rememberDatePickerState(initialSelectedDateMillis = Calendar.getInstance().timeInMillis)

        val timeDialog = remember {
            mutableStateOf(false)
        }
        val dateDialog = remember {
            mutableStateOf(false)
        }
        val timeDialog2 = remember {
            mutableStateOf(false)
        }
        val dateDialog2 = remember {
            mutableStateOf(false)
        }

        val change = remember {
            mutableStateOf(true)
        }
        var interval by remember {
            mutableIntStateOf(0)
        }
        var color by remember {
            mutableIntStateOf(0)
        }

        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.termin))
            }
        ) { it ->
            var name by remember { mutableStateOf(TextFieldValue()) }
            Column(
                Modifier
                    .padding(it)
                    .padding(top = 16.dp)
            ) {
                IconRowTexField(
                    icon = Icons.Default.Add,
                    value = name,
                    placeholderText = stringResource(id = R.string.add_name),
                    changeValue = { name = it },
                )
                Spacer(modifier = Modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.size(10.dp))
                IconRow(icon = Icons.Default.AccessTime) {
                    Paragraph1(
                        text = stringResource(R.string.add_time),
                        modifier = it.padding(16.dp)
                    )
                }
                DateTimeRow(dateDialog, datePickerState, timeDialog, timePickerState)
                DateTimeRow(dateDialog2, datePickerState2, timeDialog2, timePickerState2)
                Spacer(modifier = Modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.size(10.dp))
                IconRow(icon = Icons.Default.Repeat) {
                    IntervalDropDown(
                        it,
                        this@AddCalendarEntryActivity,
                        OPTION_INTERVAL[interval]
                    ) { idx -> interval = idx }
                }
                IconRow(
                    icon = Icons.Default.ColorLens
                ) {
                    ColorDropDown(
                        it,
                        this@AddCalendarEntryActivity,
                        color,
                    ) { c -> color = c }
                }
                Spacer(modifier = Modifier.size(10.dp))
                HorizontalDivider()
            }
        }
        DateDialog(dateDialog, datePickerState)
        TimeDialog(timeDialog, change, timePickerState)

        DateDialog(dateDialog2, datePickerState2)
        TimeDialog(timeDialog2, change, timePickerState2)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DateTimeRow(
        dateDialog: MutableState<Boolean>,
        datePickerState: DatePickerState,
        timeDialog: MutableState<Boolean>,
        timePickerState: TimePickerState
    ) {
        IconRow { mod ->
            Row(
                mod.padding(start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(onClick = { dateDialog.value = true }) {
                    val dateFormat =
                        SimpleDateFormat("EEE, d. MMM yyyy", Locale.getDefault())
                    if (datePickerState.selectedDateMillis != null) {
                        val selectedDate = Calendar.getInstance().apply {
                            timeInMillis = datePickerState.selectedDateMillis!!
                        }
                        Paragraph1(text = dateFormat.format(selectedDate.time))
                    }
                }
                TextButton(onClick = { timeDialog.value = true }) {
                    val dateFormat =
                        DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
                    val pattern = if (dateFormat.format(Calendar.getInstance().time)
                            .contains("AM") || dateFormat.format(Calendar.getInstance().time)
                            .contains("PM")
                    ) {
                        "hh a"
                    } else {
                        "HH:mm"
                    }
                    val formatter =
                        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

                    Paragraph1(
                        textAlign = TextAlign.Center,
                        text = formatter.format(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        )
                    )
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TimeDialog(
        dialog: MutableState<Boolean>,
        change: MutableState<Boolean>,
        timePickerState: TimePickerState
    ) {
        if (dialog.value) {
            PickerDialog(
                onDismissRequest = { dialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialog.value = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            dialog.value = false
                        }
                    ) { Text(stringResource(R.string.cancel)) }
                },
                switchButton = {
                    IconButton(onClick = { change.value = !change.value }) {
                        Icon(
                            imageVector = if (change.value) Icons.Default.Keyboard else Icons.Default.AccessTime,
                            contentDescription = null
                        )
                    }
                }
            )
            {
                if (change.value)
                    TimePicker(state = timePickerState)
                else
                    TimeInput(state = timePickerState)
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun DateDialog(
        dialog: MutableState<Boolean>,
        datePickerState: DatePickerState
    ) {
        if (dialog.value) {
            PickerDialog(
                title = "",
                onDismissRequest = { dialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialog.value = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            dialog.value = false
                        }
                    ) { Text(stringResource(R.string.cancel)) }
                },
            )
            {
                DatePicker(state = datePickerState)
            }
        }
    }

    @Composable
    fun PickerDialog(
        title: String = stringResource(R.string.select_time),
        onDismissRequest: () -> Unit,
        confirmButton: @Composable (() -> Unit),
        switchButton: @Composable (() -> Unit)? = null,
        dismissButton: @Composable (() -> Unit)? = null,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        content: @Composable () -> Unit,
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = containerColor
                    ),
                color = containerColor
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (title != "")
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            text = title,
                            style = MaterialTheme.typography.labelMedium
                        )
                    content()
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) {
                        if (switchButton != null)
                            switchButton()
                        Spacer(modifier = Modifier.weight(1f))
                        dismissButton?.invoke()
                        confirmButton()
                    }
                }
            }
        }
    }
}