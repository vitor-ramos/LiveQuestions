package dev.vitorramos.livequestions.composables

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.model.SiteData

interface MainContentEvents : SheetContentEvents, AppBarContentEvents, BodyContentEvents

@ExperimentalMaterialApi
@Composable
fun MainContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tags: List<String>,
    selectedTag: String?,
    tagsSearch: String,
    siteData: SiteData,
    questions: List<Question>,
    loading: Boolean,
    events: MainContentEvents,
) = BottomSheetScaffold(
    scaffoldState = bottomSheetScaffoldState,
    sheetContent = {
        SheetContent(
            bottomSheetScaffoldState.bottomSheetState,
            tags,
            selectedTag,
            tagsSearch,
            events,
            ChipStyling(siteData),
        )
    },
    topBar = {
        TopAppBar(backgroundColor = Color.White) {
            AppBarContent(
                siteData,
                events
            )
        }
    }
) { BodyContent(questions, siteData) }
