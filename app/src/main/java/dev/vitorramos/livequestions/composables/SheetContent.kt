package dev.vitorramos.livequestions.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.convert
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.ui.colorSecondaryText

interface SheetContentEvents {
    fun onSelectTag(tag: String)
    fun onChangeTagSearch(value: String)
    fun onClickTagSearch()
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SheetContent(
    tags: List<String>,
    selectedTag: String?,
    searchValue: String,
    bottomSheetState: BottomSheetState,
    chipStyling: ChipStyling,
    events: SheetContentEvents,
) = Column {
    Row(
        Modifier.fillMaxWidth()
            .height(BottomSheetScaffoldDefaults.SheetPeekHeight)
            .clickable(
                onClick = {
                    if (bottomSheetState.isCollapsed) {
                        bottomSheetState.expand()
                    } else {
                        bottomSheetState.collapse()
                    }
                }
            ),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {
        val maxInput =
            (AmbientConfiguration.current.screenHeightDp - 56) * AmbientDensity.current.density
        val input = bottomSheetState.offset.value
        val angle = -convert(input, 0f, maxInput, 0f, 180f)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(56.dp), Alignment.Center) {
                LocalImage(
                    R.drawable.ic_down,
                    Modifier.size(32.dp).graphicsLayer(rotationZ = angle)
                )
            }
            Text(getString(R.string.filter_by_tag), color = colorSecondaryText)
        }
        @OptIn(ExperimentalAnimationApi::class)
        Box(Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = selectedTag?.isNotBlank() == true,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Chip(selectedTag ?: "", chipStyling = chipStyling) {
                    events.onSelectTag("")
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = selectedTag?.isNotBlank() != true,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(getString(R.string.no_filter), color = colorSecondaryText)
            }
        }
    }
    TextField(
        searchValue,
        events::onChangeTagSearch,
        Modifier.fillMaxWidth(),
        placeholder = {
            Text(getString(R.string.search_tags))
        },
        onImeActionPerformed = { _, keyboard ->
            keyboard?.hideSoftwareKeyboard()
            events.onClickTagSearch()
        },
        trailingIcon = {
            TextButton(events::onClickTagSearch) {
                Image(Icons.Filled.Search)
            }
        },
        backgroundColor = Color(0xFFF4F4F4),
    )
    if (tags.isNotEmpty()) {
        LazyColumnForIndexed(tags, Modifier.fillMaxSize()) { index, it ->
            Row(Modifier.fillMaxWidth().clickable(onClick = {
                events.onSelectTag(it)
                bottomSheetState.collapse()
            })) {
                val modifier = Modifier.padding(
                    16.dp,
                    if (index == 0) 16.dp else 8.dp,
                    0.dp,
                    if (index == tags.size - 1) 16.dp else 8.dp,
                )
                Chip(it, modifier, chipStyling)
            }
        }
    } else {
        Box(Modifier.fillMaxSize().padding(16.dp), Alignment.TopCenter) {
            ListLoadingIndicator()
        }
    }
}
