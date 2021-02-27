package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.labeledHexToLong
import dev.vitorramos.livequestions.model.SiteData

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    chipStyling: ChipStyling,
    action: (() -> Unit)? = null,
) {
    Surface(
        if (action != null) modifier.clickable(onClick = action) else modifier,
        color = chipStyling.backgroundColor,
    ) {
        Row(Modifier.padding(12.dp, 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = chipStyling.foregroundColor)
            if (action != null) {
                Spacer(Modifier.width(4.dp))
                Surface(
                    Modifier.size(16.dp),
                    shape = CircleShape,
                    color = chipStyling.foregroundColor,
                ) {
                    Image(
                        painterResource(R.drawable.ic_close_light_gray),
                        null,
                        Modifier.padding(2.dp),
                    )
                }
            }
        }
    }
}

data class ChipStyling(val backgroundColor: Color, val foregroundColor: Color) {
    @OptIn(ExperimentalUnsignedTypes::class)
    constructor(siteData: SiteData) : this(
        Color(siteData.styling.tagBackgroundColor.labeledHexToLong()),
        Color(siteData.styling.tagForegroundColor.labeledHexToLong()),
    )
}
