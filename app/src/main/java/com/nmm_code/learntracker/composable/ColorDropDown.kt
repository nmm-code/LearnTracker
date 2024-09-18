package com.nmm_code.learntracker.composable

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmm_code.learntracker.pre.OPTION
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ColorDropDown(
    modifier: Modifier,
    context: Context,
    selectedColor: Pair<Int, Color>,
    onSelected: (it: Pair<Int, Color>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = context.getString(selectedColor.first),
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            textStyle = TextStyle.Default.copy(
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp,
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            OPTION.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Canvas(
                                Modifier
                                    .padding(end = 10.dp)
                                    .size(20.dp)

                            ) {
                                drawRoundRect(
                                    item.second,
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                            }
                            Paragraph1(
                                text = context.getString(item.first)
                            )
                        }
                    },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}