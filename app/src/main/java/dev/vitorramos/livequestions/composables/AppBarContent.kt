package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.SiteData

@Composable
fun AppBarContent(site: SiteData, navigation: NavController) = Row(
    Modifier.fillMaxSize().padding(16.dp, 0.dp, 0.dp, 0.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
) {
    RemoteImage(site.logoUrl, Modifier.height(32.dp)) {
        Text(site.name)
    }
    TextButton({ navigation.navigate("sites") }) {
        Text(getString(R.string.select_site))
        Spacer(Modifier.size(8.dp, 0.dp))
        LocalImage(
            R.drawable.forward,
            Modifier.align(Alignment.CenterVertically),
        )
    }
}
