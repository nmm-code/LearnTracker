package com.nmm_code.learntracker.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2

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

    @Preview(showBackground = true)
    @Composable
    fun TasksPage(modifier: Modifier = Modifier) {
        val list = listOf(
            "Todo1",
            "Todo2",
            "Todo3",
            "Todo4",
        )
        Scaffold(
            topBar = {
                TopBar(title = stringResource(id = R.string.tasks))
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = { /*TODO ADD ACTIVITY*/ },Modifier.padding(bottom = 50.dp)) {
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
                columns = GridCells.Adaptive(120.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(it)
                    .padding(32.dp),
            ){
                items(list) {
                    Surface(
                        onClick = {
                            startActivity(Intent(this@TasksActivity,TodoPage::class.java))
                        },
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .aspectRatio(2f)
                    ) {
                        Paragraph2(
                            text = it,
                            color = Color(163, 163, 163, 255),
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }

    }
}