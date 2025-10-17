package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.SiteData

@Composable
fun AppBarContent(site: SiteData, navController: NavController) = Row(
    Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp, 0.dp, 0.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
) {
    AsyncImage(
        model = site.logoUrl,
        contentDescription = site.name,
        modifier = Modifier.height(32.dp),
    )
    TextButton({ navController.navigate("sites") }) {
        Text(getString(R.string.select_site))
        Spacer(Modifier.size(8.dp, 0.dp))
        Image(
            painterResource(R.drawable.forward),
            null,
            Modifier.align(Alignment.CenterVertically),
        )
    }
}
