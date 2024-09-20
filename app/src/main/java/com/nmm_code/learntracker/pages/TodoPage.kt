package com.nmm_code.learntracker.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.data.Todo
import com.nmm_code.learntracker.data.TodoData
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoPage : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                Todos()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Todos(modifier: Modifier = Modifier) {
        var idx = -1
        runBlocking {
            idx = DataStoreState(this@TodoPage, DataStoreState.TODO_ID).get(0)
        }
        val todos = TodoData.read<Todo>(this).toMutableList()

        var title by remember {
           mutableStateOf(TextFieldValue(todos[idx].title))
        }
        var text by remember {
            mutableStateOf(TextFieldValue(todos[idx].message.joinToString("\n")))
        }
        BackHandler {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }
        Scaffold(
            topBar = {
                TopBar(title = "", onClick = {
                    startActivity(Intent(this, TasksActivity::class.java))
                    finish()
                })
            },
        ) { pv ->
            Column(Modifier.padding(pv)) {
                Spacer(modifier = Modifier.padding(10.dp))
                BasicTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        lifecycleScope.launch {
                            todos[idx] = Todo(title.text, text.text.split('\n'))
                            TodoData.save(this@TodoPage, todos)
                        }
                    },
                    visualTransformation = VisualTransformation.None,
                    textStyle = TextStyle(
                        fontSize = 27.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .fillMaxWidth(),
                    cursorBrush = SolidColor(Color.LightGray),
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (title.text.isEmpty()) {
                                Text(
                                    text = "Title",
                                    color = if (isSystemInDarkTheme()) Color(0xFF969EBD) else Color.Gray,
                                    fontSize = 27.sp,
                                )
                            }
                        }
                        innerTextField()
                    })

                BasicTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        lifecycleScope.launch {
                            todos[idx] = Todo(title.text, text.text.split('\n'))
                            TodoData.save(this@TodoPage, todos)
                        }
                    },
                    visualTransformation = VisualTransformation.None,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(start = 25.dp, top = 10.dp),
                    cursorBrush = SolidColor(Color.LightGray),
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (text.text.isEmpty()) {
                                Text(
                                    text = "Note",
                                    color = if (isSystemInDarkTheme()) Color(0xFF969EBD) else Color.Gray,
                                    fontSize = 18.sp,

                                    )
                            }
                        }
                        innerTextField()
                    })
            }
        }
    }
}