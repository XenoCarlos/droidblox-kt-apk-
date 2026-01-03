package com.drake.droidblox.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ExtendedTextField(
    title: String,
    subtitle: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    default: String = "",
    onLostFocus: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(default) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TitleWithSubtitle(title, subtitle)
        }
        OutlinedTextField(
            value = text,
            singleLine = true,
            maxLines = 1,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .onFocusChanged {
                    if (!it.isFocused) onLostFocus(text)
                }
        )
    }
}