package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.ui.core.ExtendedTheme


@Preview
@Composable
fun CreateTodoDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = ExtendedTheme.colors.supportSeparator,
    )
}