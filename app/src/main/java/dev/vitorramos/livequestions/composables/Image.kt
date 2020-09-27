package dev.vitorramos.livequestions.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadVectorResource
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun LocalImage(@DrawableRes id: Int, modifier: Modifier = Modifier) =
    loadVectorResource(id).resource.resource?.let { Image(it, modifier) } ?: Unit

@Composable
fun RemoteImage(
    url: String?, modifier: Modifier = Modifier,
    fallback: @Composable () -> Unit = {},
) = if (url != null) CoilImage(url, modifier, error = { fallback() }) else fallback()
