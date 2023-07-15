package com.shadi777.todoapp.ui.screen.CreateTaskScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.shadi777.todoapp.ui.core.ExtendedTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CreateTodoBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetState: ModalBottomSheetState,
    sheetShape: Shape = BottomSheetDefaults.ExpandedShape,
    sheetBackgroundColor: Color = ExtendedTheme.colors.backPrimary,
    content: @Composable () -> Unit
) = ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetShape = sheetShape,
    sheetBackgroundColor = sheetBackgroundColor,
    content = content,
    sheetContent = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 20.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomSheetDefaults.DragHandle()
            sheetContent()
        }
    }
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberTodoBottomSheetState() =
    rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
