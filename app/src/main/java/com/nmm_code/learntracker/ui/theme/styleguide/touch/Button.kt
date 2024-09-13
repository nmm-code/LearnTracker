package com.nmm_code.learntracker.ui.theme.styleguide.touch

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nmm_code.learntracker.ui.theme.space
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph2H

@Composable
fun ButtonFill(modifier: Modifier = Modifier, onClick: () -> Unit = { }, text: String) {
    Button(onClick, modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = MaterialTheme.space.padding5)) {
        Paragraph2(text = text)
    }
}

@Composable
fun ButtonOutline(modifier: Modifier = Modifier, onClick: () -> Unit = { }, text: String) {
    OutlinedButton(onClick, modifier, contentPadding = PaddingValues(horizontal = MaterialTheme.space.padding5)) {
        Paragraph2H(text = text)
    }
}