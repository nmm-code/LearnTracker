package com.nmm_code.learntracker.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nmm_code.learntracker.ui.theme.styleguide.text.Headline2
import com.nmm_code.learntracker.ui.theme.styleguide.text.Paragraph1

@Composable
fun ConfirmAlert(name: String, confirm: Boolean, onConfirm: () -> Unit, onClose: () -> Unit) {
    if (confirm)
        AlertDialog(onDismissRequest = onClose, title = {
            Headline2(text = "Delete Entry")
        }, confirmButton = {
            Button(onClick = {
                onClose()
                onConfirm()
            }) {
                Text(text = "Ok", color = Color.White)
            }
        }, text = {
            Paragraph1(
                text = "Are you sure to delete (${name.trim()})?",
                softWrap = true
            )
        })
}

@Composable
fun AlertName(alert: Boolean, title: String, mes: String, change: () -> Unit) {
    if (alert)
        AlertDialog(onDismissRequest = change, title = {
            Headline2(text = title)
        }, confirmButton = {}, text = {
            Paragraph1(
                text = mes,
                softWrap = true
            )
        })
}
