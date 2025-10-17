package dev.vitorramos.livequestions.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.convert
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.ui.colorSecondaryText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface SheetContentEvents {
    fun onSelectTag(tag: String)
    fun onChangeTagSearch(value: String)
    fun onClickTagSearch()
}

@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
)
fun SheetContent(
    tags: List<String>,
    selectedTag: String?,
    searchValue: String,
    bottomSheetState: BottomSheetState,
    chipStyling: ChipStyling,
    events: SheetContentEvents,
) = Column {
    Row(
        Modifier
            .fillMaxWidth()
            .height(BottomSheetScaffoldDefaults.SheetPeekHeight)
            .clickable(
                onClick = {
                    GlobalScope.launch {
                        if (bottomSheetState.isCollapsed) bottomSheetState.expand()
                        else bottomSheetState.collapse()
                    }
                }
            ),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {
//        TODO
//        val maxInput =
//            (LocalConfiguration.current.screenHeightDp - 56) * LocalDensity.current.density
//        val input = bottomSheetState.offset.value
//        val angle = -convert(input, 0f, maxInput, 0f, 180f)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(56.dp), Alignment.Center) {
                Image(
                    painterResource(R.drawable.ic_down),
//                    if (angle > 90) "Fechar tags" else "Abrir tags",
                    if (bottomSheetState.isExpanded) "Fechar tags" else "Abrir tags",
                    Modifier
                        .size(32.dp)
//                        .graphicsLayer(rotationZ = angle),
                )
            }
            Text(getString(R.string.filter_by_tag), color = colorSecondaryText)
        }
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
    val softKeyboardController = LocalSoftwareKeyboardController.current
    TextField(
        searchValue,
        events::onChangeTagSearch,
        Modifier.fillMaxWidth(),
        placeholder = {
            Text(getString(R.string.search_tags))
        },
        keyboardActions = KeyboardActions(onAny = {
//            softKeyboardController?.hideSoftwareKeyboard() TODO
            events.onClickTagSearch()
        }),
        trailingIcon = {
            TextButton(events::onClickTagSearch) {
                Image(Icons.Filled.Search, "Pesquisar")
            }
        },
        colors = textFieldColors(backgroundColor = Color(0xFFF4F4F4)),
    )
    if (tags.isNotEmpty()) {
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(tags) { index, it ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            events.onSelectTag(it)
                            GlobalScope.launch { bottomSheetState.collapse() }
                        })
                ) {
                    val modifier = Modifier.padding(
                        16.dp,
                        if (index == 0) 16.dp else 8.dp,
                        0.dp,
                        if (index == tags.size - 1) 16.dp else 8.dp,
                    )
                    Chip(it, modifier, chipStyling)
                }
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp), Alignment.TopCenter
        ) {
            ListLoadingIndicator()
        }
    }
}
