package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.R
import com.shadi777.todoapp.ui.core.AppTheme
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction

@Composable
fun CreateTodoDeleteButton(
    isUpdating: Boolean,
    onAction: (TodoAction) -> Unit,
    returnAction: () -> Unit
) {

    TextButton(
        onClick = { 
            onAction(TodoAction.DeleteTask)
            returnAction()
          },
        modifier = Modifier.padding(horizontal = 4.dp),
        enabled = isUpdating,
        colors = ButtonDefaults.textButtonColors(
            contentColor = ExtendedTheme.colors.red,
            disabledContentColor = ExtendedTheme.colors.labelDisable
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_button),
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(R.string.delete_button),
            style = ExtendedTheme.typography.body
        )
    }
}


@Preview
@Composable
private fun DeleteButtonEnabledPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoDeleteButton(
            isUpdating = true,
            onAction = {},
            returnAction = {}
        )
    }
}

@Preview
@Composable
private fun DeleteButtonDisabledPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoDeleteButton(
            isUpdating = false,
            onAction = {},
            returnAction = {}
        )
    }
}
