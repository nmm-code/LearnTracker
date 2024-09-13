package com.nmm_code.learntracker.ui.theme.styleguide.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Headline1(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Left,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontWeight: FontWeight= FontWeight.Normal,
    size: TextUnit = 25.sp,
    wrap:Boolean = false,
) {
    Text(
        text,
        fontWeight = fontWeight,
        fontSize = size,
        letterSpacing = 1.sp,
        color = color,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        softWrap = wrap,
        modifier = modifier
    )
}

@Composable
fun Headline2(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Left,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontWeight: FontWeight = FontWeight.Normal,
    size: TextUnit = 25.sp,
    wrap:Boolean = false,
) {
    Text(
        text,
        fontWeight = fontWeight,
        fontSize = size,
        modifier = modifier,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        softWrap =  wrap,
        color = color,
    )
}
