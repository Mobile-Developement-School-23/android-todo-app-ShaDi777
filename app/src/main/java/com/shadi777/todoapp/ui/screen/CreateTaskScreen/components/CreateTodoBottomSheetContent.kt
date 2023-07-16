package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components


import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.AppTheme
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


@Composable
fun CreateTodoBottomSheetContent(
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit
) {

    val priorityOptions = stringArrayResource(R.array.priorities).toList()

    Text(
        text = stringResource(R.string.priority),
        color = ExtendedTheme.colors.labelPrimary,
        style = ExtendedTheme.typography.titleSmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    val border = BorderStroke(1.dp, ExtendedTheme.colors.supportSeparator)
    val red = ExtendedTheme.colors.red
    val blue = ExtendedTheme.colors.blue
    val selectedColor = remember { Animatable(blue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var selectorOption by remember {
            mutableStateOf(priorityOptions[todoItem.priority.ordinal])
        }
        val scope = rememberCoroutineScope()
        MultiSelector(
            options = priorityOptions,
            selectedOption = selectorOption,
            onOptionSelect = {
                selectorOption = it
                val newPriority = when(it) {
                    priorityOptions[0] -> Priority.Low
                    priorityOptions[2] -> Priority.High
                    else -> Priority.Default
                }

                if (newPriority != todoItem.priority) {
                    onAction(TodoAction.UpdatePriority(newPriority))
                    todoItem.priority = newPriority
                    scope.coroutineContext.cancelChildren()
                    scope.launch {
                        if (newPriority == Priority.High) {
                            selectedColor.animateTo(red, repeatable(1, tween(800), RepeatMode.Restart))
                        }
                        selectedColor.animateTo(blue, tween(800))
                    }
                }
            },
            modifier = Modifier
                .border(border, CircleShape)
                .fillMaxWidth(0.9f)
                .height(80.dp),
            selectionColor = selectedColor.value
        )
    }


    Spacer(modifier = Modifier.height(6.dp))
}


@Preview
@Composable
private fun BottomSheetContentHighPriorityPreview() {
    AppTheme(darkTheme = false) {
        CreateTodoBottomSheetContent(
            todoItem =
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = "Has text",
                    priority = Priority.High,
                    isDone = false,
                    color = null,
                    createDate = Date().time,
                    changeDate = Date().time
                ),
            onAction = {}
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun BottomSheetContentDefaultPriorityPreview() {
    AppTheme(darkTheme = true) {
        CreateTodoBottomSheetContent(
            todoItem =
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "Has text",
                priority = Priority.Default,
                isDone = false,
                color = null,
                createDate = Date().time,
                changeDate = Date().time
            ),
            onAction = {}
        )
    }
}