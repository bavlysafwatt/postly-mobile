package com.example.postly.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme


enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

@Composable
fun PostlyTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = rememberDynamicColorScheme(
        seedColor = PostlySeedColor,
        isDark = useDarkTheme,
        style = PaletteStyle.TonalSpot
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PostlyTypography,
        shapes = PostlyShapes,
        content = content
    )
}