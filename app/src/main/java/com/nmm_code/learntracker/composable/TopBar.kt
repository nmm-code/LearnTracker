package com.nmm_code.learntracker.composable

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.nmm_code.learntracker.pages.MainActivity
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String, icon: Boolean = true,
    onClick: (() -> Unit)? = null,
    actions: (() -> Unit)? = null,
    actionsImageVector: ImageVector = Icons.Default.FilterAlt
) {
    val context: Context = LocalContext.current
    CenterAlignedTopAppBar(
        title = { Headline2(text = title) },
        colors = TopAppBarColors(
            Color.Transparent,
            Color.Transparent,
            MaterialTheme.colorScheme.onBackground,
            Color.Transparent,
            Color.Transparent,
        ),
        navigationIcon = {
            if (icon)
                IconButton(onClick = {
                    if (onClick == null) {
                        context.startActivity(Intent(context, MainActivity::class.java))
                        (context as Activity).finish()
                    } else
                        onClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back icons"
                    )
                }
        },
        actions = {
            if (actions != null)
                IconButton(onClick = actions) {
                    Icon(
                        imageVector = actionsImageVector,
                        contentDescription = "action icons",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
        }
    )
}