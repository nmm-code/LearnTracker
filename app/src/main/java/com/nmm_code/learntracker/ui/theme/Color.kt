package com.nmm_code.learntracker.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

val primary = Color(33, 118, 174)
val secondary = Color(87, 184, 255)

val tiger = Color(182, 109, 13)
val ternary = Color(251, 177, 60)
val contrast = Color(254, 104, 71)
val gray = Color(153, 143, 143, 255)

fun calculateLuminance(color: Color): Double {
    val r = color.red.let { if (it <= 0.03928f) it / 12.92 else ((it + 0.055) / 1.055).pow(2.4) }
    val g = color.green.let { if (it <= 0.03928f) it / 12.92 else ((it + 0.055) / 1.055).pow(2.4) }
    val b = color.blue.let { if (it <= 0.03928f) it / 12.92 else ((it + 0.055) / 1.055).pow(2.4) }

    return 0.2126 * r + 0.7152 * g + 0.0722 * b
}


fun contrastRatio(color1: Color, color2: Color): Double {
    val lum1 = calculateLuminance(color1)
    val lum2 = calculateLuminance(color2)
    return (max(lum1, lum2) + 0.05) / (min(lum1, lum2) + 0.05)
}

fun getAccessibleTextColor(backgroundColor: Color): Color {
    val whiteContrast = contrastRatio(backgroundColor, Color.White)
    val blackContrast = contrastRatio(backgroundColor, Color.Black)

    return if (whiteContrast > blackContrast) Color.White else Color.Black
}