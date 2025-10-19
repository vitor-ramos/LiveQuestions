package dev.vitorramos.livequestions.composables

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.vitorramos.livequestions.R
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.model.SiteData

@ExperimentalMaterial3Api
@Composable
fun Questions(
    site: SiteData,
    questions: List<Question>,
    tag: String?,
    navController: NavController,
    events: SheetContentEvents,
    chipStyling: ChipStyling,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppBarContent(
                        site,
                        navController,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .clickable {
                        navController.navigate("tags")
                    },
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                Text(getString(R.string.filter_by_tag), Modifier.padding(start = 16.dp))
                Box(
                    Modifier
                        .height(56.dp)
                        .padding(0.dp, 0.dp, 16.dp, 0.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = tag != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Chip(tag ?: "", chipStyling = chipStyling) {
                            events.onSelectTag(null)
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = tag == null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Text(getString(R.string.no_filter))
                    }
                }
            }
        },
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            items(questions) {
                QuestionCard(
                    it,
                    ChipStyling(site),
                )
            }
        }
    }
}
