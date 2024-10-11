package com.nmm_code.learntracker.pre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.composable.TopBar
import com.nmm_code.learntracker.ui.theme.LearnTrackerTheme
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1
import com.nmm_code.learntracker.ui.theme.styleguide.touch.ButtonOutline

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
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.privacy), icon = false)
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 40.dp)
                ) {

                    item(key = 1) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.p1ff)
                        )
                    }
                    item(key = 2) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 3) {
                        Headline2(wrap = true, text = stringResource(R.string.sfdfssdf))
                    }
                    item(key = 4) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 5) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.fsdfsdf)
                        )
                    }
                    item(key = 6) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 7) {
                        Headline2(
                            wrap = true,
                            text = stringResource(R.string.dsfsdf)
                        )
                    }
                    item(key = 8) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 9) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.dfgdfg)
                        )
                    }
                    item(key = 10) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 11) {
                        Headline2(wrap = true, text = stringResource(R.string.gfdgfdgdfg))
                    }
                    item(key = 12) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 13) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.ggf)
                        )
                    }
                    item(key = 14) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 15) {
                        Headline2(wrap = true, text = stringResource(R.string.fgdhghdf))
                    }
                    item(key = 16) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 17) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.fdfgfgfdg)
                        )
                    }
                    item(key = 18) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 19) {
                        Headline2(wrap = true, text = stringResource(R.string.dfgdfgg))
                    }
                    item(key = 20) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 21) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.fdgdgfg)
                        )
                    }
                    item(key = 22) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 23) {
                        Headline2(wrap = true, text = stringResource(R.string.fbdfgdfgdf))
                    }
                    item(key = 24) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 25) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.bgbfg)
                        )
                    }
                    item(key = 26) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 27) {
                        Headline2(wrap = true, text = stringResource(R.string.sdfsdfsd))
                    }
                    item(key = 28) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 29) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.fdssfsd)
                        )
                    }
                    item(key = 30) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }

                    item(key = 31) {
                        Headline2(wrap = true, text = stringResource(R.string.dfsgdfsgsdgd))
                    }
                    item(key = 32) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 33) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.sdfgsdgsd)
                        )
                    }
                    item(key = 34) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 35) {
                        Headline2(wrap = true, text = stringResource(R.string.gfddgf))
                    }
                    item(key = 36) {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    item(key = 37) {
                        Paragraph1(
                            softWrap = true,
                            text = stringResource(R.string.fsddfgdfg)
                        )
                    }
                    item(key = 38) {
                        Spacer(modifier = Modifier.padding(30.dp))
                    }

                    item(key = 39) {
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
