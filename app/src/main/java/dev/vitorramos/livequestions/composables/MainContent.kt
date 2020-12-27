package dev.vitorramos.livequestions.composables

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.model.SiteData

@ExperimentalMaterialApi
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
) = BottomSheetScaffold(
    scaffoldState = bottomSheetScaffoldState,
    sheetContent = {
        SheetContent(
            tags,
            tag,
            tagsSearch,
            bottomSheetScaffoldState.bottomSheetState,
            ChipStyling(site),
            events,
        )
    },
    topBar = {
        TopAppBar(backgroundColor = Color.White) {
            AppBarContent(
                site,
                navController,
            )
        }
    }
) {
    LazyColumnFor(questions) {
        QuestionCard(
            it,
            ChipStyling(site),
        )
    }
}
