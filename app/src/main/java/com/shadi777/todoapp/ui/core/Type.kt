package com.shadi777.todoapp.ui.core

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ExtendedAppTypography = ExtendedTypography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),

    title = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
        lineHeight = 32.sp
    ),

    button = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),

    body = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),

    subhead = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)