package dev.vitorramos.livequestions.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = Color.Gray,
//    primaryVariant = Color.DarkGray, TODO
)

@Composable
fun LiveQuestionsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
