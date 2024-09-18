package com.nmm_code.learntracker.pre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline1
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.touch.ButtonOutline
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SelectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dataStore = DataStoreState(this@SelectActivity, DataStoreState.SELECT_INFO)

        lifecycleScope.launch {
            DataStoreState(this@SelectActivity, DataStoreState.PAGE).set(1)
        }

        setContent {
            LearnTrackerTheme {
                InfoDialog(dataStoreState = dataStore)
                SelectActivityPage()
            }
        }
    }

    @Composable
    fun InfoDialog(
        modifier: Modifier = Modifier,
        dataStoreState: DataStoreState<Boolean>
    ) {
        var enabled by remember {
            mutableStateOf(false)
        }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            delay(200)
            enabled = dataStoreState.get(true)
        }

        val onClose = {
            enabled = false
            coroutineScope.launch {
                dataStoreState.set(false)
            }
        }


        if (enabled)
            AlertDialog(
                onDismissRequest = { onClose() },
                confirmButton = { ButtonOutline(text = stringResource(R.string.i_got_it), onClick = { onClose() }) },
                title = {
                    Headline2(text = stringResource(R.string.information))
                },
                text = {
                    Column {
                        Paragraph1(
                            fontWeight = FontWeight.W400,
                            text = stringResource(R.string.these_are_just_different_profiles_you_can_set_up),
                            softWrap = true
                        )
                        Spacer(modifier = modifier.padding(10.dp))
                        Paragraph1(
                            fontWeight = FontWeight.W400,
                            text = stringResource(R.string.you_can_switch_between_them_anytime_and_choose_the_one_that_suits_your_needs),
                            softWrap = true,
                        )
                    }
                }
            )
    }

    data class SelectBox(
        val title: String,
        val path: String,
        val icon: ImageVector
    )

    @Preview(showBackground = true)
    @Composable
    fun SelectActivityPage(modifier: Modifier = Modifier) {
        val list = listOf(
            SelectBox(
                stringResource(R.string.school),
                "/school",
                Icons.Default.Apartment
            ),
            SelectBox(
                stringResource(R.string.university),
                "/uni",
                Icons.Default.School
            ), SelectBox(
                stringResource(R.string.apprentice_ship),
                "/ap",
                Icons.Default.HomeWork
            ), SelectBox(
                stringResource(R.string.work),
                "/work",
                Icons.Default.Work
            )
        )

        Surface {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.align(Alignment.Center)) {
                    Headline1(
                        text = stringResource(R.string.select),
                        size = 35.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier.padding(start = 32.dp, bottom = 4.dp)
                    )
                    Headline2(
                        text = stringResource(R.string.your_profile),
                        color = Color.LightGray,
                        size = 25.sp,
                        modifier = Modifier.padding(start = 32.dp, bottom = 32.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(180.dp),
                        Modifier.padding(24.dp)
                    ) {
                        itemsIndexed(list) { idx, item ->
                            BoxElement(idx, item)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BoxElement(
        idx: Int,
        item: SelectBox
    ) {
        val coroutineScope = rememberCoroutineScope()
        Surface(
            onClick = {
                startActivity(Intent(this, WorkingTitleActivity::class.java))
                coroutineScope.launch {
                    val data = DataStoreState(this@SelectActivity, DataStoreState.PATH)
                    data.set(item.path)
                }
            },
            color = if (idx % 3 != 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .aspectRatio(1f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Box {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(60.dp)
                        .align(Alignment.TopEnd)

                )
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                        .height(50.dp)
                ) {
                    val name = item.title.split(" ")
                    Headline1(
                        text = name[0],
                        color = Color.White, size = 21.sp,
                        wrap = true,
                    )
                    if (name.size == 2)
                        Headline1(
                            text = name[1],
                            color = Color.White, size = 21.sp,
                            wrap = true,
                        )
                }
            }
        }
    }

}