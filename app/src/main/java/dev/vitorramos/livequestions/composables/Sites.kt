package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.SiteData
import dev.vitorramos.livequestions.ui.colorDivider
import dev.vitorramos.livequestions.ui.colorSecondaryText

interface SitesListContentEvents {
    fun onChangeSitesSearch(value: String)
    fun onSelectSite(site: SiteData)
    fun onClickClose()
}

@Composable
fun SitesListContent(
    sites: List<SiteData>,
    searchBarValue: String,
    showCloseButton: Boolean,
    events: SitesListContentEvents,
) = Column {
    SearchBar(searchBarValue, events::onChangeSitesSearch, showCloseButton, events::onClickClose)
    LazyColumnFor(sites) {
        SiteItem(it, events::onSelectSite)
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    showCloseButton: Boolean,
    onCloseClick: () -> Unit,
) = TextField(
    value,
    onValueChange,
    Modifier.fillMaxWidth(),
    leadingIcon = {
        if (showCloseButton) TextButton(onCloseClick) { LocalImage(R.drawable.back) }
    },
    textStyle = TextStyle(fontSize = 16.sp),
    onImeActionPerformed = { _, keyboard ->
        keyboard?.hideSoftwareKeyboard()
    },
    placeholder = {
        Text(getString(R.string.search_sites))
    }
)

@Composable
private fun SiteItem(
    site: SiteData,
    onSiteSelected: (SiteData) -> Unit,
) = Column(Modifier.fillMaxWidth().clickable(onClick = { onSiteSelected(site) })) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(site.iconUrl, Modifier.size(56.dp), error = {
            LocalImage(R.drawable.ic_image_not_loaded)
        })
        Spacer(Modifier.width(8.dp))
        Column {
            Text(site.name)
            Text(site.audience, color = colorSecondaryText)
        }
    }
    Divider(Modifier.fillMaxWidth().height(1.dp), colorDivider)
}
