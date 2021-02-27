package dev.vitorramos.livequestions.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallback: @Composable () -> Unit = {},
) = if (url != null) {
    CoilImage(
        url,
        contentDescription,
        modifier,
        error = { fallback() },
    )
} else {
    fallback()
}
