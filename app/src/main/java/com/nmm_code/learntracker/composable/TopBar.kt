package com.nmm_code.learntracker.composable

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nmm_code.learntracker.pages.MainActivity
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier, title: String, icon: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val context: Context = LocalContext.current
    CenterAlignedTopAppBar(
        title = { Headline2(text = title) },
        colors = TopAppBarColors(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background,
        ),
        navigationIcon = {
            if (icon)
                IconButton(onClick = {
                    if (onClick == null) {
                        context.startActivity(Intent(context,MainActivity::class.java))
                        (context as Activity).finish()
                    } else
                        onClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back icons"
                    )
                }
        }
    )
}