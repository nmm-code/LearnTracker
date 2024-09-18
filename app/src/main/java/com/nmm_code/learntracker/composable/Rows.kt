package com.nmm_code.learntracker.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun IconRowTexField(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: TextFieldValue,
    placeholderText: String,
    changeValue: (it: TextFieldValue) -> Unit
) {
    IconRow(icon) {
        TextField(
            value = value,
            onValueChange = changeValue,
            textStyle = TextStyle.Default.copy(
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp,
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = {
                Paragraph1(
                    text = placeholderText
                )
            },
            modifier = it.composed { modifier }
        )
    }
}


@Composable
fun IconRow(
    icon: ImageVector,
    content: @Composable (Modifier) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            Modifier.weight(3f)
        )
        content(Modifier.weight(8f))
        Spacer(modifier = Modifier.weight(1f))
    }
}