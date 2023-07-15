package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.AppTheme
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction
import java.util.Date
import java.util.UUID


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTodoScreen(
    isUpdating: Boolean,
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit,
    returnAction: () -> Unit
) {
    val listState = rememberLazyListState()
    val sheetState = rememberTodoBottomSheetState()

    CreateTodoBottomSheetLayout(
        sheetContent = {
            CreateTodoBottomSheetContent(
                todoItem = todoItem,
                onAction = onAction
            )
        },
        sheetState = sheetState
    ) {

        Scaffold(
            topBar = {
                CreateTodoTopAppBar(
                    todoItem = todoItem,
                    onAction = onAction,
                    returnAction = returnAction
                )
            },
            containerColor = ExtendedTheme.colors.backPrimary
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = listState
            ) {
                item {
                    CreateTodoEditText(todoItem, onAction)

                    CreateTodoPriorityPicker(
                        todoItem = todoItem,
                        sheetState = sheetState
                    )

                    CreateTodoDivider()

                    CreateTodoDatePicker(
                        todoItem = todoItem,
                        onAction = onAction
                    )

                    CreateTodoDivider()

                    CreateTodoDeleteButton(
                        isUpdating = isUpdating,
                        onAction = onAction,
                        returnAction = returnAction
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
        }
    }
}


@Preview
@Composable
private fun ScreenLightPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoScreen(
            isUpdating = true,
            todoItem =
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = "This is task text",
                    priority = Priority.High,
                    isDone = false,
                    color = null,
                    createDate = Date().time,
                    changeDate = Date().time,
                    deadlineDate = Date().time + 86_400_000
                ),
            onAction = {},
            returnAction = {}
        )
    }
}

@Preview
@Composable
private fun ScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        CreateTodoScreen(
            isUpdating = true,
            todoItem =
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "This is task text",
                priority = Priority.High,
                isDone = false,
                color = null,
                createDate = Date().time,
                changeDate = Date().time,
                deadlineDate = Date().time + 86_400_000
            ),
            onAction = {},
            returnAction = {}
        )
    }
}
