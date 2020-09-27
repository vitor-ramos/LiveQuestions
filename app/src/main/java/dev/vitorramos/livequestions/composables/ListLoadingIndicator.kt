package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListLoadingIndicator() = Surface(
        Modifier.size(36.dp),
        shape = CircleShape,
        elevation = 4.dp,
) {
    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 3.dp)
}
