package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme

class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackerTheme {
                CalendarPage()

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CalendarPage(modifier: Modifier = Modifier) {
        TopBar(title = stringResource(R.string.calendar))
    }
}