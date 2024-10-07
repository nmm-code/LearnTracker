package com.nmm_code.learntracker.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.composable.ColorDropDown
import com.nmm_code.learntracker.composable.IconRow
import com.nmm_code.learntracker.composable.IconRowTexField
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.pre.OPTION
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme

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

    @Composable
    fun Page(modifier: Modifier = Modifier) {
        Scaffold(
            topBar = {
                TopBar(title = "Termin")
            }
        ) { it ->
            Column(Modifier.padding(it).padding(top = 16.dp)) {
                IconRowTexField(
                    icon = Icons.Default.TextFields,
                    value = TextFieldValue(),
                    placeholderText = "Add a new Name",
                    changeValue = {

                    }
                )

                IconRow(
                    icon = Icons.Default.ColorLens
                ) {mod ->
                    ColorDropDown(
                        mod,
                        this@AddCalendarEntryActivity,
                        OPTION[0]
                    ) { it -> it.first.dp}
                }
            }
        }
    }
}