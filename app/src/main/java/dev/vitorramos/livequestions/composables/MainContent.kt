package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.model.SiteData

@ExperimentalMaterial3Api
@Composable
fun MainContent(
    site: SiteData,
    questions: List<Question>,
    tags: List<String>,
    tag: String?,
    tagsSearch: String,
    loading: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navController: NavController,
    events: SheetContentEvents,
) {
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            SheetContent(
                modifier = Modifier, // TODO
                tags = tags,
                selectedTag = tag,
                searchValue = tagsSearch,
                bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                chipStyling = ChipStyling(site),
                events = events,
            )
        },
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
        }
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
