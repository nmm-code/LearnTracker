package com.nmm_code.learntracker.pre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.AlertName
import com.nmm_code.learntracker.composable.ColorDropDown
import com.nmm_code.learntracker.composable.IconRow
import com.nmm_code.learntracker.composable.IconRowTexField
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.data.WorkingTitle
import com.nmm_code.learntracker.data.WorkingTitleData
import com.nmm_code.learntracker.data.WorkingTitleType
import com.nmm_code.learntracker.pages.MainActivity
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.io.path.ExperimentalPathApi

private const val BOX_WIDTH = 150

val OPTION = listOf(
    Pair(R.string.blue, Color(57, 96, 187)),
    Pair(R.string.red, Color(160, 51, 51)),
    Pair(R.string.green, Color(18, 83, 11)),
    Pair(R.string.orange, Color(175, 160, 18)),
    Pair(R.string.black, Color(1, 1, 1))
)

class WorkingTitleActivity : ComponentActivity() {

    private var type: WorkingTitleType? = null
    var data: WorkingTitleData = WorkingTitleData

    private suspend fun getPath() =
        DataStoreState(this@WorkingTitleActivity, DataStoreState.PATH).get("")


    private fun navigatePageBack() =
        lifecycleScope.launch {
            val string = getPath().split("/")
            val mergedPath = string.dropLast(1).joinToString("/")
            if (mergedPath.isEmpty())
                startActivity(Intent(this@WorkingTitleActivity, SelectActivity::class.java))
            else
                startActivity(Intent(this@WorkingTitleActivity, WorkingTitleActivity::class.java))
            DataStoreState(
                this@WorkingTitleActivity,
                DataStoreState.PATH
            ).set(mergedPath)
        }

    @Composable
    private fun getTitleByPage(): String {
        val string = DataStoreState(this, DataStoreState.PATH).getAsState(default = "/").value
        return when (string) {
            "/school" -> {
                type = WorkingTitleType.SCHOOL
                stringResource(R.string.classes)
            }

            "/uni" -> {
                type = WorkingTitleType.UNI
                stringResource(R.string.degrees)
            }

            "/ap" -> {
                type = WorkingTitleType.AP
                stringResource(R.string.apprenticeships)
            }

            "/work" -> {
                type = WorkingTitleType.WORK
                stringResource(R.string.work_places)
            }

            else -> ""
        }
    }

    private fun getList() = data.read<WorkingTitle>(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            DataStoreState(this@WorkingTitleActivity, DataStoreState.PAGE).set(2)
            println(
                DataStoreState(this@WorkingTitleActivity, DataStoreState.PATH).get("")
            )
        }

        setContent {
            LearnTrackerTheme {
                WorkingTitlePage()
            }
        }
    }

    @Composable
    private fun AddButton(onClick: () -> Unit) {
        Surface(
            onClick,
            color = Color.LightGray,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .aspectRatio(1f)
                .padding(50.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = Color.White
                )
            }
        }
    }

    @Composable
    fun WorkingTitlePage() {
        val title = getTitleByPage()
        val list = getList().toMutableStateList()
        var isModalOpen by remember { mutableIntStateOf(-1) }

        var animate by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(Unit) {
            animate = true
        }
        Scaffold(
            topBar = {
                AnimatedVisibility(visible = animate, enter = fadeIn(tween(500))) {
                    TopBar(title = title) { navigatePageBack() }
                }
            },
            bottomBar = {
                AddModalSheet(isModalOpen, list) { isModalOpen = -1 }
            }
        ) {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(BOX_WIDTH.dp),
                Modifier
                    .padding(it)
                    .padding(MaterialTheme.space.padding6)
            ) {
                for (i in list.indices) {
                    val item = list[i]
                    if (item.type == type)
                        item {
                            BoxElement(item) { isModalOpen = i }
                        }
                }
                item {
                    AddButton { isModalOpen = Int.MAX_VALUE }
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
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPathApi::class)
    private fun AddModalSheet(
        index: Int,
        list: SnapshotStateList<WorkingTitle>,
        onClose: () -> Unit
    ) {
        // ALERT INFO
        var alert by remember {
            mutableStateOf(false)
        }
        var confirm by remember {
            mutableStateOf(false)
        }
        var confirmTitle by remember {
            mutableStateOf("")
        }

        AlertName(
            alert,
            stringResource(id = R.string.invalid_name),
            stringResource(id = R.string.the_name_needs_at_least_4_characters)
        ) { alert = false }

        val isModalOpen = index != -1
        val focusRequester = remember { FocusRequester() }

        ConfirmAlert(confirmTitle, confirm, {
            onClose()
            confirmTitle = ""
            val path = list[index].path
            runBlocking {
                try {
                    val file = File(
                        application.filesDir.path + DataStoreState(
                            this@WorkingTitleActivity,
                            DataStoreState.PATH
                        ).get("") + path
                    )
                    file.deleteRecursively()
                    println("Delete Result")
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("File Deletion Error" + e.message.toString())
                }
            }

            list.removeAt(index)
            data.save(this@WorkingTitleActivity, list)
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

                val entry = if (isEditing) list[index] else null

                var name by remember {
                    mutableStateOf(
                        if (isAdding) TextFieldValue() else TextFieldValue(
                            entry!!.name
                        )
                    )
                }

                var alias by remember {
                    mutableStateOf(
                        if (isAdding) TextFieldValue() else TextFieldValue(
                            entry!!.alias
                        )
                    )
                }

                var selectedColor by remember {
                    // new entry or edit entry
                    mutableStateOf(if (isAdding) OPTION[0] else OPTION.find {
                        it.second.toArgb() == entry!!.color
                    } ?: OPTION[0])
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
                            if (name.text.length <= 3) {
                                alert = true
                                return@IconButton
                            }
                            lifecycleScope.launch {
                                if (isAdding) {
                                    list.add(
                                        WorkingTitle(
                                            name.text,
                                            alias.text,
                                            selectedColor.second.toArgb(),
                                            type = type ?: WorkingTitleType.WORK
                                        )
                                    )
                                } else {
                                    list[index] =
                                        WorkingTitle(
                                            name.text,
                                            alias.text,
                                            selectedColor.second.toArgb(),
                                            type = type ?: WorkingTitleType.WORK
                                        )
                                }
                                data.save(this@WorkingTitleActivity, list)
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
                    icon = Icons.Default.ModeEdit,
                    value = name,
                    placeholderText = stringResource(id = R.string.add_name),
                    modifier = Modifier.focusRequester(focusRequester)
                ) { name = it }
                IconRowTexField(
                    icon = Icons.AutoMirrored.Filled.ShortText,
                    value = alias,
                    placeholderText = stringResource(R.string.add_alias),

                    ) {
                    alias = it
                }
                IconRow(
                    icon = Icons.Default.ColorLens
                ) {
                    ColorDropDown(
                        it,
                        this@WorkingTitleActivity,
                        selectedColor
                    ) { color -> selectedColor = color }
                }
            }
    }

    @Composable
    fun ConfirmAlert(name: String, confirm: Boolean, onConfirm: () -> Unit, onClose: () -> Unit) {
        if (confirm)
            AlertDialog(onDismissRequest = onClose, title = {
                Headline2(text = stringResource(R.string.delete_entry))
            }, confirmButton = {
                Button(onClick = {
                    onClose()
                    onConfirm()
                }) {
                    Text(text = stringResource(R.string.ok), color = Color.White)
                }
            }, text = {
                Paragraph1(
                    text = stringResource(R.string.are_you_sure_to_delete, name.trim()),
                    softWrap = true
                )
            })
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun BoxElement(item: WorkingTitle, openModal: () -> Unit) {
        Surface(
            color = Color(item.color),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .aspectRatio(1f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .combinedClickable(
                    onClick = {

                        lifecycleScope.launch {
                            startActivity(
                                Intent(
                                    this@WorkingTitleActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                            DataStoreState(
                                this@WorkingTitleActivity,
                                DataStoreState.PATH
                            ).set(getPath() + item.path)
                        }
                    },
                    onLongClick = {
                        openModal()
                    },
                )
        ) {
            Box {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                        .height(50.dp)
                ) {
                    Headline1(
                        text = item.name,
                        color = Color.White, size = 21.sp,
                        wrap = true,
                    )
                }
            }
        }
    }
}