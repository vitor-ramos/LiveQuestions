package dev.vitorramos.livequestions.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
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
    ExperimentalMaterial3Api::class,
)
fun SheetContent(
    modifier: Modifier,
    tags: List<String>,
    selectedTag: String?,
    searchValue: String,
    bottomSheetState: SheetState,
    chipStyling: ChipStyling,
    events: SheetContentEvents,
) {
    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(BottomSheetDefaults.SheetPeekHeight)
                .clickable(
                    onClick = {
//                  TODO
                        GlobalScope.launch {
                            if (bottomSheetState.currentValue != SheetValue.Expanded) bottomSheetState.expand()
                            else bottomSheetState.hide()
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
                    val contentDescription =
                        if (bottomSheetState.currentValue == SheetValue.Expanded) {
                            "Fechar tags"
                        } else {
                            "Abrir tags"
                        }
                    Image(
                        painter = painterResource(id = R.drawable.ic_down),
//                    if (angle > 90) "Fechar tags" else "Abrir tags",
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .size(32.dp)
//                        .graphicsLayer(rotationZ = angle),
                    )
                }
                Text(getString(R.string.filter_by_tag))
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
                    Text(getString(R.string.no_filter))
                }
            }
        }
        LocalSoftwareKeyboardController.current
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
        )
        if (tags.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxSize()) {
                itemsIndexed(tags) { index, it ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                events.onSelectTag(it)
//                          TODO
                                GlobalScope.launch { bottomSheetState.hide() }
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
}
