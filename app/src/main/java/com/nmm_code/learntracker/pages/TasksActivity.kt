package com.nmm_code.learntracker.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.ConfirmAlert
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.data.Todo
import com.nmm_code.learntracker.data.TodoData
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import kotlinx.coroutines.runBlocking

class TasksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                TasksPage()
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Preview(showBackground = true)
    @Composable
    fun TasksPage(modifier: Modifier = Modifier) {
        val list = TodoData.read<Todo>(this).toMutableStateList()
        var alert by remember {
            mutableStateOf(false)
        }
        if (alert) {
            var idx = 0
            runBlocking {
                idx = DataStoreState(this@TasksActivity, DataStoreState.TODO_ID).get(idx)
            }
            ConfirmAlert(name = list[idx].title, confirm = true, onConfirm = {
                alert = false
                list.removeAt(idx)
                TodoData.save(this@TasksActivity, list)
            }) { alert = false }
        }
        Scaffold(
            topBar = {
                TopBar(title = stringResource(id = R.string.tasks))
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        list.add(Todo("", listOf("")))
                        TodoData.save(this@TasksActivity, list)
                    },
                    Modifier.padding(bottom = 50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = stringResource(R.string.add_new_todolist))
                }
            }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(it)
                    .padding(32.dp),
            ) {
                itemsIndexed(list) { idx, it ->
                    Surface(
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .aspectRatio(2f)
                            .combinedClickable(
                                onClick = {
                                    runBlocking {
                                        DataStoreState(
                                            this@TasksActivity,
                                            DataStoreState.TODO_ID
                                        ).set(idx)
                                    }
                                    startActivity(Intent(this@TasksActivity, TodoPage::class.java))
                                },
                                onLongClick = {
                                    runBlocking {
                                        DataStoreState(
                                            this@TasksActivity,
                                            DataStoreState.TODO_ID
                                        ).set(idx)
                                    }
                                    alert = true
                                }
                            )
                    ) {
                        Paragraph2(
                            text = it.title + '\n' + it.message.joinToString("\n"),
                            softWrap = true,
                            color = Color(163, 163, 163, 255),
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }

    }
}