package com.nmm_code.learntracker.ui.theme.styleguide.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun Paragraph1(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.W300,
    color: Color = MaterialTheme.colorScheme.onBackground,
    softWrap: Boolean = false,
    textAlign: TextAlign = TextAlign.Left
) {
    Text(
        text,
        fontWeight = fontWeight,
        fontSize = 18.sp,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        softWrap = softWrap,
        letterSpacing = 0.5.sp,
        color = color,
        modifier = modifier
    )
}

@Composable
fun Paragraph2(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.W300,
    color: Color = MaterialTheme.colorScheme.onBackground,
    softWrap: Boolean = false,
    textAlign: TextAlign = TextAlign.Left
) {
    Text(
        text,
        fontWeight = fontWeight,
        fontSize = 15.sp,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        softWrap = softWrap,
        letterSpacing = 0.5.sp,
        color = color,
        modifier = modifier
    )
}

@Composable
fun Paragraph1H(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.W600,
    color: Color = MaterialTheme.colorScheme.onBackground,
    softWrap: Boolean = false,
    textAlign: TextAlign = TextAlign.Left
) {
    Text(
        text, fontWeight = fontWeight, fontSize = 18.sp, textAlign = textAlign, overflow = TextOverflow.Ellipsis,
        softWrap = softWrap, letterSpacing = 0.5.sp, color = color, modifier = modifier
    )
}

@Composable
fun Paragraph2H(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.W600,
    color: Color = MaterialTheme.colorScheme.onBackground,
    softWrap: Boolean = false,
    textAlign: TextAlign = TextAlign.Left
) {
    Text(
        text, fontWeight = fontWeight, fontSize = 15.sp, textAlign = textAlign,overflow = TextOverflow.Ellipsis,
        softWrap = softWrap, letterSpacing = 0.5.sp, color = color, modifier = modifier
    )
}