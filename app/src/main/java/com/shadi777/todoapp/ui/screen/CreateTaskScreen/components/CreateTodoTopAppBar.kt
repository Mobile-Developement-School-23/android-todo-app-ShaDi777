package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTodoTopAppBar(
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit,
    returnAction: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .shadow(2.dp),
        navigationIcon = {
            IconButton(
                onClick = { returnAction() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        actions = {
            TextButton(
                onClick = {
                    onAction(TodoAction.SaveTask)
                    returnAction()
                  },
                enabled = todoItem.text.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ExtendedTheme.colors.blue,
                    disabledContentColor = ExtendedTheme.colors.labelDisable
                )
            ) {
                Text(
                    text = stringResource(R.string.save_button),
                    style = ExtendedTheme.typography.button
                )
            }
        },
        title = {  },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            scrolledContainerColor = ExtendedTheme.colors.backElevated,
            navigationIconContentColor = ExtendedTheme.colors.labelPrimary
        )
    )
}