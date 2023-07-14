package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun openBottomSheet(
    sheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        if (!sheetState.isVisible) {
            sheetState.show()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTodoPriorityPicker(
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit,
    sheetState: ModalBottomSheetState
) {
    var clicked: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                clicked = true
            }
            .padding(4.dp)
    ) {
        if (clicked) {
            if (sheetState.isVisible) clicked = false
            openBottomSheet(sheetState = sheetState)
        }
        Text(
            text = stringResource(R.string.priority),
            color = ExtendedTheme.colors.labelPrimary,
            style = ExtendedTheme.typography.body
        )
        Text(
            text = stringArrayResource(R.array.priorities)[todoItem.priority.ordinal],
            modifier = Modifier.padding(top = 2.dp),
            color =
                if (todoItem.priority == Priority.High)
                    ExtendedTheme.colors.red
                else
                    ExtendedTheme.colors.labelTertiary,
            style = ExtendedTheme.typography.subhead
        )
    }
}