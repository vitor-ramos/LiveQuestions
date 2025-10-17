package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.SiteData
import dev.vitorramos.livequestions.ui.colorDivider
import dev.vitorramos.livequestions.ui.colorSecondaryText

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
) = Column {
    SearchBar(searchBarValue, events::onChangeSitesSearch, showCloseButton, navController)
    LazyColumn {
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

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    showCloseButton: Boolean,
    navController: NavController,
) {
    val softKeyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value,
        onValueChange,
        Modifier.fillMaxWidth(),
        leadingIcon = {
            if (showCloseButton) TextButton({ navController.popBackStack() }) {
                Image(painterResource(R.drawable.back), "Voltar")
            }
        },
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardActions = KeyboardActions {
//            softKeyboardController?.hideSoftwareKeyboard() TODO
        },
        placeholder = {
            Text(getString(R.string.search_sites))
        }
    )
}

@Composable
private fun SiteItem(
    site: SiteData,
    onSiteSelected: (SiteData) -> Unit,
) = Column(
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
//        TODO
//        CoilImage(site.iconUrl, null, Modifier.size(56.dp), error = {
//            Image(painterResource(R.drawable.ic_image_not_loaded), null)
//        })
//        Spacer(Modifier.width(8.dp))
        Column {
            Text(site.name)
            Text(site.audience, color = colorSecondaryText)
        }
    }
    Divider(
        Modifier
            .fillMaxWidth()
            .height(1.dp), colorDivider
    )
}
