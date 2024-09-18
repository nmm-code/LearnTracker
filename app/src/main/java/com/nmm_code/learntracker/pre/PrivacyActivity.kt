package com.nmm_code.learntracker.pre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import com.nmm_code.learntracker.ui.theme.styleguide.touch.ButtonOutline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class PrivacyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearnTrackerTheme {
                PrivacyPage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PrivacyPage(modifier: Modifier = Modifier) {
        val items = remember {
            listOf(
                "Lorem ipsum dolor sit amet. eiusmod tempor incididunt ut labore et dolore magn",
                "Consectetur adipiscing elit. eiusmod tempor incididunt ut labore et dolore magn",
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident.",
                "Sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Lorem ipsum dolor sit amet. eiusmod tempor incididunt ut labore et dolore magn",
                "Consectetur adipiscing elit. eiusmod tempor incididunt ut labore et dolore magn",
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident.",
                "Sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Lorem ipsum dolor sit amet. eiusmod tempor incididunt ut labore et dolore magn",
                "Consectetur adipiscing elit. eiusmod tempor incididunt ut labore et dolore magn",
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident.",
                "Sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Consectetur adipiscing elit. eiusmod tempor incididunt ut labore et dolore magn",
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident.",
                "Sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Lorem ipsum dolor sit amet. eiusmod tempor incididunt ut labore et dolore magn",
                "Consectetur adipiscing elit. eiusmod tempor incididunt ut labore et dolore magn",
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                "Excepteur sint occaecat cupidatat non proident.",
                "Sunt in culpa qui officia deserunt mollit anim id est laborum."
            )
        }
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.privacy), icon = false)
            },
            content = { paddingValues ->
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(items) {
                        Paragraph2(
                            text = it,
                            softWrap = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 2.dp)
                        )
                    }
                    item {
                        ButtonOutline(
                            onClick = {
                                this@PrivacyActivity.startActivity(
                                    Intent(
                                        this@PrivacyActivity,
                                        SelectActivity::class.java
                                    )
                                )
                                finish()
                            },
                            text = stringResource(R.string.accept),
                            modifier = Modifier.padding(bottom = MaterialTheme.space.padding7)
                        )
                    }
                }
            })
    }


}
