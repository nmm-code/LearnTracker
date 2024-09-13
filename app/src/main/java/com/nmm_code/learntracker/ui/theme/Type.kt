package com.nmm_code.learntracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.nmm_code.learntracker.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Inter")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = fontFamily),
    displayMedium = TextStyle(fontFamily = fontFamily),
    displaySmall = TextStyle(fontFamily = fontFamily),
    headlineLarge = TextStyle(fontFamily = fontFamily),
    headlineMedium = TextStyle(fontFamily = fontFamily),
    headlineSmall = TextStyle(fontFamily = fontFamily),
    titleLarge = TextStyle(fontFamily = fontFamily),
    titleMedium = TextStyle(fontFamily = fontFamily),
    titleSmall = TextStyle(fontFamily = fontFamily),
    bodyLarge = TextStyle(fontFamily = fontFamily),
    bodyMedium = TextStyle(fontFamily = fontFamily),
    bodySmall = TextStyle(fontFamily = fontFamily),
    labelLarge = TextStyle(fontFamily = fontFamily),
    labelMedium = TextStyle(fontFamily = fontFamily),
    labelSmall = TextStyle(fontFamily = fontFamily),
)