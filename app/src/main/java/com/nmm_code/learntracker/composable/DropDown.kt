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
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1

val OPTION_COLOR = listOf(
    Pair(R.string.blue, Color(57, 96, 187)),
    Pair(R.string.red, Color(160, 51, 51)),
    Pair(R.string.green, Color(47, 221, 29, 255)),
    Pair(R.string.orange, Color(255, 163, 3, 255)),
    Pair(R.string.magenta, Color(67, 13, 175, 255)),
    Pair(R.string.pink, Color(194, 54, 117, 255)),
    Pair(R.string.cyan, Color(22, 197, 162, 255)),
    Pair(R.string.yellow, Color(234, 255, 0, 255)),
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ColorDropDown(
    modifier: Modifier,
    context: Context,
    selectedColor: Int,
    onSelected: (it: Int) -> Unit,
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
            value = context.getString(OPTION_COLOR[selectedColor].first),
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
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor =  Color.Transparent,
                disabledIndicatorColor =Color.Transparent,
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
            OPTION_COLOR.forEachIndexed { idx, item ->
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
                        onSelected(idx)
                        expanded = false
                    }
                )
            }
        }
    }
}

val OPTION_INTERVAL = listOf(
    R.string.doesn_t_repeat,
   R.string.every_week,
    R.string.every_month,
    R.string.every_year,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IntervalDropDown(
    modifier: Modifier,
    context: Context,
    selectedInterval: Int,
    onSelected: (it: Int) -> Unit,
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
            value = context.getString(selectedInterval),
            onValueChange = { } ,
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
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor =  Color.Transparent,
                disabledIndicatorColor =Color.Transparent,
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
            OPTION_INTERVAL.forEachIndexed{ idx, item ->
                DropdownMenuItem(
                    text = {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Paragraph1(
                                text = context.getString(item)
                            )
                        }
                    },
                    onClick = {
                        onSelected(idx)
                        expanded = false
                    }
                )
            }
        }
    }
}