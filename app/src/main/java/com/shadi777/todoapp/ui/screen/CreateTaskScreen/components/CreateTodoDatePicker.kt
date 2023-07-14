package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.datepicker.CalendarConstraints
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun CreateTodoDatePicker(
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit
) {
    val date = todoItem.deadlineDate ?: 0
    var isDateVisible by remember {
        mutableStateOf(todoItem.deadlineDate != null)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp)
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(date) {
            DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .format(
                    Instant.ofEpochMilli(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
        }
        var openDialog by remember { mutableStateOf(false) }

        DatePicker(
            todoItem = todoItem,
            open = openDialog,
            closePicker = {
                if (todoItem.deadlineDate == null) {
                    isDateVisible = false
                }
                openDialog = false
            },
            onAction = onAction
        )

        Column {
            Text(
                text = stringResource(R.string.finish_until),
                modifier = Modifier.padding(start = 4.dp),
                color = ExtendedTheme.colors.labelPrimary,
                style = ExtendedTheme.typography.body
            )

            AnimatedVisibility(visible = isDateVisible) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { openDialog = true }
                        .padding(4.dp)
                ) {
                    Text(
                        text = dateText,
                        color = ExtendedTheme.colors.blue,
                        style = ExtendedTheme.typography.subhead
                    )
                }
            }
        }

        Switch(
            checked = isDateVisible,
            onCheckedChange = {
                isDateVisible = !isDateVisible
                if (it) openDialog = true
                else todoItem.deadlineDate = null
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ExtendedTheme.colors.blue,
                checkedTrackColor = ExtendedTheme.colors.blueTranslucent,
                uncheckedThumbColor = ExtendedTheme.colors.backElevated,
                uncheckedTrackColor = ExtendedTheme.colors.supportOverlay,
                uncheckedBorderColor = ExtendedTheme.colors.supportOverlay,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    todoItem: TodoItem,
    open: Boolean,
    closePicker: () -> Unit,
    onAction: (TodoAction) -> Unit
) {
    if (open) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                todoItem.deadlineDate ?: System.currentTimeMillis(),
            )
        val confirmEnabled by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closePicker,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            todoItem.deadlineDate = it
                            onAction(TodoAction.UpdateDate(it))
                        }
                        closePicker()
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.save_button), color = ExtendedTheme.colors.blue)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closePicker
                ) {
                    Text(stringResource(R.string.cancel_button), color = ExtendedTheme.colors.blue)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = ExtendedTheme.colors.backPrimary,
                    selectedDayContainerColor = ExtendedTheme.colors.blue,
                    selectedDayContentColor = ExtendedTheme.colors.labelPrimaryReversed,
                    todayContentColor = ExtendedTheme.colors.labelPrimary
                )
            )
        }
    }
}