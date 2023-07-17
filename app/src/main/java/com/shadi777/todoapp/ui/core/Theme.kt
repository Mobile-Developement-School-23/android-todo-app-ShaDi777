package com.shadi777.todoapp.ui.core

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat


private val LightColors = lightColorScheme(
    primary = LightBackPrimary,
    onPrimary = LightLabelPrimary,
    secondary = LightBackSecondary,
    onSecondary = LightLabelSecondary,
    onTertiary = LightLabelTertiary,
    background = LightBackPrimary,
    onBackground = LightLabelPrimary,
    outline = LightSupportSeparator,
    scrim = LightGrayExtra,
)


private val DarkColors = darkColorScheme(
    primary = DarkBackPrimary,
    onPrimary = DarkLabelPrimary,
    secondary = DarkBackSecondary,
    onSecondary = DarkLabelSecondary,
    onTertiary = DarkLabelTertiary,
    background = DarkBackPrimary,
    onBackground = DarkLabelPrimary,
    outline = DarkSupportSeparator,
    scrim = DarkGrayExtra,
)


@Immutable
data class ExtendedColors(
    val supportSeparator: Color = Color.Unspecified,
    val supportOverlay: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    val labelPrimaryReversed: Color = Color.Unspecified,
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backElevated: Color = Color.Unspecified,
    val red: Color = Color.Unspecified,
    val green: Color = Color.Unspecified,
    val blue: Color = Color.Unspecified,
    val blueTranslucent: Color = Color.Unspecified,
    val grayExtra: Color = Color.Unspecified
)

@Immutable
data class ExtendedTypography(
    val titleLarge: TextStyle = TextStyle.Default,
    val title: TextStyle = TextStyle.Default,
    val titleSmall: TextStyle = TextStyle.Default,
    val button: TextStyle = TextStyle.Default,
    val body: TextStyle = TextStyle.Default,
    val subhead: TextStyle = TextStyle.Default
)

val lightExtendedColors = ExtendedColors(
    supportSeparator = LightSupportSeparator,
    supportOverlay = LightSupportOverlay,
    labelPrimary = LightLabelPrimary,
    labelPrimaryReversed = DarkLabelPrimary,
    labelSecondary = LightLabelSecondary,
    labelTertiary = LightLabelTertiary,
    labelDisable = LightLabelDisable,
    backPrimary = LightBackPrimary,
    backSecondary = LightBackSecondary,
    backElevated = LightBackElevated,
    red = LightRed,
    green = LightGreen,
    blue = LightBlue,
    blueTranslucent = LightBlueTranslucent,
    grayExtra = LightGrayExtra
)

val darkExtendedColors = ExtendedColors(
    supportSeparator = DarkSupportSeparator,
    supportOverlay = DarkSupportOverlay,
    labelPrimary = DarkLabelPrimary,
    labelPrimaryReversed = LightLabelPrimary,
    labelSecondary = DarkLabelSecondary,
    labelTertiary = DarkLabelTertiary,
    labelDisable = DarkLabelDisable,
    backPrimary = DarkBackPrimary,
    backSecondary = DarkBackSecondary,
    backElevated = DarkBackElevated,
    red = DarkRed,
    green = DarkGreen,
    blue = DarkBlue,
    blueTranslucent = DarkBlueTranslucent,
    grayExtra = DarkGrayExtra
)

val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }
val LocalExtendedTypography = staticCompositionLocalOf { ExtendedTypography() }


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) darkExtendedColors else lightExtendedColors
    val extendedTypography = ExtendedAppTypography

    val colorScheme = when {
        darkTheme -> DarkColors
        else -> LightColors
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}

object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current

    val typography: ExtendedTypography
        @Composable
        get() = LocalExtendedTypography.current
}