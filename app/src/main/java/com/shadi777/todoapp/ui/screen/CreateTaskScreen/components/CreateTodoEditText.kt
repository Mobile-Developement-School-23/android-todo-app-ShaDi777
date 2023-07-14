package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.ui.core.ExtendedTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction


@Composable
fun CreateTodoEditText(
    todoItem: TodoItem,
    onAction: (TodoAction) -> Unit
) {
    Surface(
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        BasicTextField(

            value = todoItem.text,
            onValueChange = { onAction(TodoAction.UpdateText(it)) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = ExtendedTheme.colors.labelPrimary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            minLines = 3,
            cursorBrush = SolidColor(ExtendedTheme.colors.labelPrimary)
        ) { innerTextField ->

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = ExtendedTheme.colors.backSecondary,
                    contentColor = ExtendedTheme.colors.labelTertiary
                )
            ) {

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    if (todoItem.text.isEmpty())
                        Text(
                            text = stringResource(R.string.edit_text_hint),
                            style = ExtendedTheme.typography.body,
                            color = ExtendedTheme.colors.labelTertiary
                        )
                    innerTextField.invoke()
                }
            }
        }
    }
}