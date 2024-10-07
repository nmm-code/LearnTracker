package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.AlertName
import com.nmm_code.learntracker.composable.ColorDropDown
import com.nmm_code.learntracker.composable.ConfirmAlert
import com.nmm_code.learntracker.composable.IconRow
import com.nmm_code.learntracker.composable.IconRowTexField
import com.nmm_code.learntracker.composable.OPTION_COLOR
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.Subject
import com.nmm_code.learntracker.data.SubjectsData
import com.nmm_code.learntracker.data.TimerActivityData
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import kotlinx.coroutines.launch

class SubjectsActivity : ComponentActivity() {
    private val data = SubjectsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearnTrackerTheme {
                SubjectsPage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SubjectsPage(modifier: Modifier = Modifier) {
        val list = remember {
            data.getList(this).toMutableStateList()
        }
        var isModalOpen by remember { mutableIntStateOf(-1) }

        Scaffold(topBar = {
            TopBar(title = stringResource(id = R.string.subjects))
        },
            bottomBar = {
                AddModalSheet(list, index = isModalOpen) { isModalOpen = -1 }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { isModalOpen = Int.MAX_VALUE },
                    Modifier.padding(bottom = 50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = stringResource(R.string.add_new_subject))
                }
            }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(it)
                    .padding(32.dp)
            ) {
                list.forEachIndexed { idx, item ->

                    val color = Color(item.color)

                    item {
                        Surface(
                            onClick = { isModalOpen = idx },
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(color)
                                .aspectRatio(4f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                            ) {
                                Headline2(
                                    text = item.title,
                                    color = getAccessibleTextColor(Color(item.color)),
                                    wrap = true,
                                    size = 20.sp,
                                    modifier = Modifier
                                        .padding(start = 30.dp)
                                        .align(Alignment.CenterStart)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *  @param index is variable which has 2 meaning
     *      isModal == -1 -> modal is closed
     *      isModal == Int.MAX_VALUE -> Modal open with new Values
     *      else -> Modal is idx of list and on long click edit Modal
     */
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun AddModalSheet(list: SnapshotStateList<Subject>, index: Int, onClose: () -> Unit) {

        var alert by remember {
            mutableStateOf(false)
        }
        var confirm by remember {
            mutableStateOf(false)
        }
        var confirmTitle by remember {
            mutableStateOf("")
        }
        val isModalOpen = index != -1

        AlertName(
            alert,
            stringResource(R.string.invalid_name),
            stringResource(R.string.the_name_needs_at_least_4_characters)
        ) { alert = false }

        ConfirmAlert(confirmTitle, confirm, {
            onClose()
            confirmTitle = ""
            lifecycleScope.launch {
                list.removeAt(index)

                val dataTimer = TimerActivityData
                var idx = 0
                val timers = dataTimer.getList(this@SubjectsActivity).filter { it.id != index }.map {
                    it.id = idx
                    idx++
                    it
                }
                dataTimer.saveList(this@SubjectsActivity, timers)

                data.saveList(this@SubjectsActivity, list)
            }
        }) { confirm = false }


        if (isModalOpen)
            ModalBottomSheet(
                onDismissRequest = onClose,
                sheetState = rememberModalBottomSheetState(),
                windowInsets = WindowInsets.ime,
                dragHandle = {},
                modifier = Modifier.height(400.dp)
            ) {
                val isAdding = index == Int.MAX_VALUE
                val isEditing = index != Int.MAX_VALUE

                val entry = if (index > -1 && index < Int.MAX_VALUE) list[index] else Subject(
                    "",
                    1,
                )

                var name by remember {
                    mutableStateOf(
                        if (isAdding) TextFieldValue() else TextFieldValue(
                            entry.title
                        )
                    )
                }

                var selectedColor by remember {
                    mutableIntStateOf(
                        if (isAdding) {
                            0
                        } else {
                            val foundIndex =
                                OPTION_COLOR.indexOfFirst { it.second.toArgb() == entry.color }

                           if (foundIndex != -1) foundIndex else 0
                        })
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    IconButton(
                        onClick = {
                            if (name.text.length <= 3 || name.text.length > 40) {
                                alert = true
                                return@IconButton
                            }
                            lifecycleScope.launch {
                                if (isAdding) {
                                    list.add(
                                        Subject(
                                            name.text,
                                            OPTION_COLOR[selectedColor].second.toArgb(),
                                        )
                                    )
                                } else {
                                    list[index] = Subject(
                                        name.text,
                                        OPTION_COLOR[selectedColor].second.toArgb(),
                                    )
                                }
                                data.saveList(this@SubjectsActivity, list)
                                onClose()
                            }
                        },
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(30.dp, 0.dp)

                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    }

                    if (isEditing)
                        IconButton(
                            onClick = {
                                confirm = true
                                confirmTitle = name.text
                            },
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.Red)
                                .padding(30.dp, 0.dp)

                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                }
                HorizontalDivider(Modifier.padding(bottom = 20.dp))

                IconRowTexField(
                    modifier = Modifier,
                    icon = Icons.Default.ModeEdit,
                    value = name,
                    placeholderText = stringResource(R.string.add_name),
                    { name = it },
                    false
                )

                IconRow(
                    icon = Icons.Default.ColorLens
                ) {
                    ColorDropDown(
                        it,
                        this@SubjectsActivity,
                        selectedColor
                    ) { color -> selectedColor = color }
                }
            }
    }

}