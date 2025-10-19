package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.SiteData

interface SitesListContentEvents {
    fun onChangeSitesSearch(value: String)
    fun onSelectSite(site: SiteData)
}

@Composable
fun Sites(
    sites: List<SiteData>,
    searchBarValue: String,
    showCloseButton: Boolean,
    navController: NavController,
    events: SitesListContentEvents,
) {
    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.statusBarsPadding(),
                value = searchBarValue,
                onValueChange = events::onChangeSitesSearch,
                showCloseButton = showCloseButton,
                navController = navController,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
        ) {
            items(sites) {
                SiteItem(it) { site ->
                    events.onSelectSite(site)
                    navController.navigate("questions") {
                        popUpTo(-1) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun SearchBar(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    showCloseButton: Boolean,
    navController: NavController,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    if (showCloseButton) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = {
                TextButton({ navController.popBackStack() }) {
                    Image(painterResource(R.drawable.back), "Voltar")
                }
            },
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            keyboardActions = KeyboardActions {
                softwareKeyboardController?.hide()
            },
            placeholder = {
                Text(getString(R.string.search_sites))
            }
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            keyboardActions = KeyboardActions {
                softwareKeyboardController?.hide()
            },
            placeholder = {
                Text(getString(R.string.search_sites))
            }
        )
    }
}

@Composable
private fun SiteItem(
    site: SiteData,
    onSiteSelected: (SiteData) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onSiteSelected(site) })
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = site.iconUrl,
                contentDescription = null,
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(site.name)
                Text(site.audience)
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
        )
    }
}
