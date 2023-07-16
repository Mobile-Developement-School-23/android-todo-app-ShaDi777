package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.AppTheme
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.UUID


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
        val timeText = remember(date) {
            DateTimeFormatter
                .ofLocalizedTime(FormatStyle.SHORT)
                .format(
                    Instant.ofEpochMilli(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                )
        }
        var openDateDialog by remember { mutableStateOf(false) }
        var openTimeDialog by remember { mutableStateOf(false) }

        DatePicker(
            todoItem = todoItem,
            open = openDateDialog,
            closePicker = {
                if (todoItem.deadlineDate == null) {
                    isDateVisible = false
                }
                openDateDialog = false
            },
            onAction = onAction
        )

        TimePicker(
            time = Instant.ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .toLocalTime(),
            open = openTimeDialog,
            closePicker = { openTimeDialog = false },
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
                Row {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { openDateDialog = true }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = dateText,
                            color = ExtendedTheme.colors.blue,
                            style = ExtendedTheme.typography.subhead
                        )
                    }

                    Text(
                        text = " | ",
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 1.dp),
                        color = ExtendedTheme.colors.labelTertiary,
                        style = ExtendedTheme.typography.subhead
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { openTimeDialog = true }
                            .padding(4.dp)
                    ) {
                        if (Instant.ofEpochMilli(date)
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime().second == 0
                        ) {
                            Text(
                                text = stringResource(R.string.select_time),
                                color = ExtendedTheme.colors.labelTertiary,
                                style = ExtendedTheme.typography.subhead
                            )
                        } else {
                            Text(
                                text = timeText,
                                color = ExtendedTheme.colors.blue,
                                style = ExtendedTheme.typography.subhead
                            )
                        }
                    }
                }
            }
        }

        Switch(
            checked = isDateVisible,
            onCheckedChange = {
                isDateVisible = !isDateVisible
                if (it) openDateDialog = true
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
            derivedStateOf {
                datePickerState.selectedDateMillis != null &&
                        datePickerState.selectedDateMillis!! + 24L * 60 * 60 * 1000 >= System.currentTimeMillis()
            }
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
                    Text(
                        stringResource(R.string.save_button),
                        color = if (confirmEnabled) ExtendedTheme.colors.blue
                        else ExtendedTheme.colors.labelDisable
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePicker(
    time: LocalTime,
    open: Boolean,
    closePicker: () -> Unit,
    onAction: (TodoAction) -> Unit
) {
    if (open) {
        val timePickerState = rememberTimePickerState(time.hour, time.minute)

        TimePickerDialog(
            title = stringResource(R.string.select_time),
            onDismiss = closePicker,
            onRemove = {
                timePickerState.let {
                    onAction(TodoAction.UpdateTime(it.hour, it.minute, 0))
                }
                closePicker()
            },
            onConfirm = {
                timePickerState.let {
                    onAction(TodoAction.UpdateTime(it.hour, it.minute, 1))
                }
                closePicker()
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelLarge
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onRemove
                    ) {
                        Text(
                            stringResource(R.string.cancel_button),
                            color = ExtendedTheme.colors.blue
                        )
                    }
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text(
                            stringResource(R.string.save_button),
                            color = ExtendedTheme.colors.blue
                        )
                    }
                }
            }
        }
    }
}


@Preview(locale = "ru")
@Composable
private fun DatePickerDialogPreview() {
    AppTheme(darkTheme = false) {
        DatePicker(
            todoItem =
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "Has text",
                priority = Priority.Default,
                isDone = false,
                color = null,
                createDate = Date().time,
                changeDate = Date().time,
                deadlineDate = Date().time + 86400000
            ),
            open = true,
            closePicker = {},
            onAction = {}
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun DatePickerEnabledPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoDatePicker(
            todoItem =
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = "Has text",
                    priority = Priority.Default,
                    isDone = false,
                    color = null,
                    createDate = Date().time,
                    changeDate = Date().time,
                    deadlineDate = Date().time + 86400000
                ),
            onAction = {}
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun DatePickerDisabledPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoDatePicker(
            todoItem =
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "Has text",
                priority = Priority.Default,
                isDone = false,
                color = null,
                createDate = Date().time,
                changeDate = Date().time,
                deadlineDate = null
            ),
            onAction = {}
        )
    }
}
