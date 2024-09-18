package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.getAccessibleTextColor
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2

class SubjectsActivity : ComponentActivity() {
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
        // TODO READ
        val list = listOf(
            Pair("Mathe", Color.Blue),
            Pair("Deutsch", Color.Red),
            Pair("NaWi", Color.DarkGray),
            Pair("English", Color.Magenta),
            Pair("Bwl", Color.Cyan),
        )
        Scaffold(topBar = {
            TopBar(title = "Subjects")
        },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = { /*TODO ADD ACTIVITY*/ },Modifier.padding(bottom = 50.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = "Add new Subject")
                }
            }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(it)
                    .padding(32.dp)
            ) {
                list.forEachIndexed { idx, item ->
                    item {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    2.dp,
                                    Color.Black,
                                    RoundedCornerShape(16.dp)
                                )
                                .background(item.second)
                                .aspectRatio(4f)
                        ) {
                            Headline1(
                                text = item.first,
                                color = getAccessibleTextColor(item.second),
                                modifier = Modifier
                                    .padding(start = 30.dp)
                                    .align(Alignment.CenterStart)
                            )
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .padding(end = 30.dp)
                                    .align(Alignment.CenterEnd)
                            ) {
                                Paragraph2(
                                    text = "18. April 1980",
                                    color = getAccessibleTextColor(item.second)
                                )
                                Paragraph2(
                                    text = "08:00",
                                    color = getAccessibleTextColor(item.second)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}