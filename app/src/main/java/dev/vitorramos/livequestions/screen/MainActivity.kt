package dev.vitorramos.livequestions.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import dev.vitorramos.livequestions.castOrNull
import dev.vitorramos.livequestions.composables.MainContent
import dev.vitorramos.livequestions.composables.SitesListContent
import dev.vitorramos.livequestions.model.SiteData
import dev.vitorramos.livequestions.model.SiteNotSelected
import dev.vitorramos.livequestions.moveToFirst
import dev.vitorramos.livequestions.ui.LiveQuestionsTheme
import dev.vitorramos.livequestions.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private var backButtonAction: BackButtonAction = BackButtonDefault

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun setContent() = setContent {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        val site by viewModel.site.observeAsState()
        val questions by viewModel.questions.observeAsState(listOf())
        val sites by viewModel.sites.observeAsState(listOf())
        val sitesSearch by viewModel.sitesSearch.observeAsState("")
        val tagsSearch by viewModel.tagsSearch.observeAsState("")
        val tags by viewModel.tags.observeAsState(listOf())
        val selectedTag by viewModel.tag.observeAsState()
        val shouldShowSites by viewModel.shouldShowSites.observeAsState(false)
        val loading by viewModel.loading.observeAsState(false)

        backButtonAction = if (shouldShowSites) BackButtonCallback(viewModel::closeSites)
        else with(bottomSheetScaffoldState.bottomSheetState) {
            if (isCollapsed || site is SiteNotSelected) BackButtonDefault
            else BackButtonCallback { collapse() }
        }

        LiveQuestionsTheme {
            when {
                site == null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
                shouldShowSites || (site is SiteNotSelected) -> SitesListContent(
                    sites.moveToFirst {
                        site.castOrNull<SiteData>()?.apiSiteParameter == it.apiSiteParameter
                    },
                    sitesSearch,
                    showCloseButton = shouldShowSites,
                    viewModel,
                )
                site is SiteData -> MainContent(
                    bottomSheetScaffoldState,
                    tags,
                    selectedTag,
                    tagsSearch,
                    site as SiteData,
                    questions,
                    loading,
                    viewModel,
                )
            }
        }
    }

    override fun onBackPressed() = when (backButtonAction) {
        is BackButtonDefault -> super.onBackPressed()
        is BackButtonCallback -> (backButtonAction as BackButtonCallback).callback()
    }
}

sealed class BackButtonAction

class BackButtonCallback(val callback: () -> Unit) : BackButtonAction()

object BackButtonDefault : BackButtonAction()
