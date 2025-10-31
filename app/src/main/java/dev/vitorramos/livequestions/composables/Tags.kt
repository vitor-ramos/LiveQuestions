package dev.vitorramos.livequestions.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString

interface SheetContentEvents {
    fun onSelectTag(tag: String?)

    fun onChangeTagSearch(value: String)

    fun onClickTagSearch()
}

@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
)
fun Tags(
    tags: List<String>,
    tag: String?,
    searchValue: String,
    chipStyling: ChipStyling,
    events: SheetContentEvents,
    navController: NavController,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    Column(Modifier.systemBarsPadding()) {
        TextField(
            searchValue,
            events::onChangeTagSearch,
            Modifier.fillMaxWidth(),
            placeholder = {
                Text(getString(R.string.search_tags))
            },
            keyboardActions =
                KeyboardActions(onAny = {
                    softwareKeyboardController?.hide()
                    events.onClickTagSearch()
                }),
            leadingIcon = {
                TextButton({ navController.navigate("questions") }) {
                    Image(Icons.AutoMirrored.Default.ArrowBack, "Voltar")
                }
            },
            trailingIcon = {
                TextButton(events::onClickTagSearch) {
                    Image(Icons.Filled.Search, "Pesquisar")
                }
            },
            singleLine = true,
        )
        if (tag != null) {
            Box(
                modifier =
                    Modifier
                        .height(56.dp)
                        .padding(start = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Chip(tag, chipStyling = chipStyling) {
                    events.onSelectTag(null)
                }
            }
            HorizontalDivider()
        }
        if (tags.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxSize()) {
                itemsIndexed(tags) { index, it ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                events.onSelectTag(it)
                                navController.navigate("questions")
                            }),
                    ) {
                        val modifier =
                            Modifier.padding(
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
                    .padding(16.dp),
                Alignment.TopCenter,
            ) {
                ListLoadingIndicator()
            }
        }
    }
}
