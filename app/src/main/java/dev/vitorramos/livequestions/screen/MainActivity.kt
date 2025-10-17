package dev.vitorramos.livequestions.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.vitorramos.livequestions.composables.MainContent
import dev.vitorramos.livequestions.composables.Sites
import dev.vitorramos.livequestions.model.SiteData
import dev.vitorramos.livequestions.ui.LiveQuestionsTheme
import dev.vitorramos.livequestions.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Content() {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        val navController = rememberNavController()

        val questions by viewModel.questions.observeAsState(listOf())
        val sites by viewModel.sites.observeAsState(listOf())
        val tags by viewModel.tags.observeAsState(listOf())

        val site by viewModel.site.observeAsState()
        val tag by viewModel.tag.observeAsState()

        val sitesSearch by viewModel.sitesSearch.observeAsState("")
        val tagsSearch by viewModel.tagsSearch.observeAsState("")

        val loading by viewModel.loading.observeAsState(false)

        LiveQuestionsTheme {
            NavHost(
                navController,
                if (site is SiteData) "questions" else "sites",
            ) {
                composable("questions") {
                    MainContent(
                        site = site as SiteData,
                        questions = questions,
                        tags = tags,
                        tag = tag,
                        tagsSearch = tagsSearch,
                        loading = loading,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        navController = navController,
                        events = viewModel,
                    )
                }
                composable("sites") {
                    Sites(
                        sites = sites,
                        searchBarValue = sitesSearch,
                        showCloseButton = site != null,
                        navController = navController,
                        events = viewModel,
                    )
                }
            }
        }
    }
}
