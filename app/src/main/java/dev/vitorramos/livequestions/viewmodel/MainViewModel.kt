package dev.vitorramos.livequestions.viewmodel

import androidx.lifecycle.*
import dev.vitorramos.livequestions.castOrNull
import dev.vitorramos.livequestions.composables.MainContentEvents
import dev.vitorramos.livequestions.composables.SitesListContentEvents
import dev.vitorramos.livequestions.model.*
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel(), SitesListContentEvents, MainContentEvents {
    private val repository by inject(Repository::class.java)

    private val tagsImpl = MediatorLiveData<List<String>>()
    val tags: LiveData<List<String>> = tagsImpl

    private val sitesSearchImpl = MutableLiveData("")
    val sitesSearch: LiveData<String> = sitesSearchImpl

    private val tagsSearchImpl = MutableLiveData("")
    val tagsSearch: LiveData<String> = tagsSearchImpl

    private val tagImpl = MutableLiveData(repository.getSelectedTag())
    val tag: LiveData<String> = tagImpl

    private val siteImpl = MutableLiveData<Site>()
    val site: LiveData<Site> = siteImpl

    private val questionsImpl = MediatorLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = questionsImpl

    private val sitesImpl = MediatorLiveData<List<SiteData>>()
    val sites: LiveData<List<SiteData>> = sitesImpl

    private val shouldShowSitesImpl = MutableLiveData<Boolean>()
    val shouldShowSites: LiveData<Boolean> = shouldShowSitesImpl

    private val loadingImpl = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = loadingImpl

    private var loadedQuestionsFromSite: String? = null
    private var loadedQuestionsWithTag: String? = null

    private var loadedTagsFromSite: String? = null
    private var loadedTagsFilteredBy: String? = null

    private var sitesFilteredBySearch: String? = null

    private var allSites = listOf<SiteData>()
    private var pageLoaded = 1

    init {
        questionsImpl.addSource(site) { site: Site? ->
            if (site is SiteData && site.apiSiteParameter != loadedQuestionsFromSite) {
                viewModelScope.launch {
                    pageLoaded = 1
                    val list = repository.getQuestions(site.apiSiteParameter, tag.value, 1)
                    loadedQuestionsFromSite = site.apiSiteParameter
                    loadedQuestionsWithTag = tag.value
                    questionsImpl.postValue(list)
                }
            }
        }
        questionsImpl.addSource(tag) { tag: String? ->
            if (tag != loadedQuestionsWithTag) {
                viewModelScope.launch {
                    pageLoaded = 1
                    val site = site.value
                    if (site is SiteData) {
                        val list = repository.getQuestions(site.apiSiteParameter, tag, 1)
                        loadedQuestionsFromSite = site.apiSiteParameter
                        loadedQuestionsWithTag = tag
                        questionsImpl.postValue(list)
                    }
                }
            }
        }
        tagsImpl.addSource(site) {
            it.castOrNull<SiteData>()?.apiSiteParameter?.let { siteId ->
                if (loadedTagsFromSite != siteId) {
                    viewModelScope.launch {
                        pageLoaded = 1
                        tagsImpl.postValue(repository.getTags(siteId, tagsSearch.value, 1))
                        loadedTagsFromSite = siteId
                        loadedTagsFilteredBy = tagsSearch.value
                    }
                }
            }
        }
        sitesImpl.addSource(sitesSearch) { search ->
            if (sitesFilteredBySearch != search) {
                sitesFilteredBySearch = search
                sitesImpl.postValue(allSites.filter { it.name.contains(search, true) })
            }
        }
        viewModelScope.launch {
            repository.getSelectedSiteId().let { siteId ->
                if (siteId.isBlank()) siteImpl.postValue(SiteNotSelected())
                else siteImpl.postValue(repository.getSite(siteId))
            }
        }
        viewModelScope.launch {
            allSites = repository.getSites()
            sitesImpl.postValue(allSites)
        }
    }

    override fun onSelectTag(tag: String) {
        tagImpl.postValue(tag)
        viewModelScope.launch { repository.selectTag(tag) }
    }

    override fun onChangeTagSearch(value: String) = tagsSearchImpl.postValue(value)

    override fun onChangeSitesSearch(value: String) = sitesSearchImpl.postValue(value)

    override fun onSelectSite(site: SiteData) {
        viewModelScope.launch {
            repository.selectSite(site.apiSiteParameter)
            tagImpl.postValue("")
            siteImpl.postValue(site)
            shouldShowSitesImpl.postValue(false)
        }
    }

    override fun onClickClose() = shouldShowSitesImpl.postValue(false)

    override fun onClickChangeSite() = shouldShowSitesImpl.postValue(true)

    fun onLoadNextPage() {
        val site = site.value
        if (site is SiteData) {
            loadingImpl.postValue(true)
            viewModelScope.launch {
                pageLoaded++
                questionsImpl.postValue(mutableListOf<Question>().apply {
                    questions.value?.let { addAll(it) }
                    addAll(repository.getQuestions(site.apiSiteParameter, tag.value, pageLoaded))
                })
                loadingImpl.postValue(false)
            }
        }
    }

    override fun onClickTagSearch() {
        site.value.castOrNull<SiteData>()?.apiSiteParameter?.let { siteId ->
            if (loadedTagsFilteredBy != tagsSearch.value) {
                viewModelScope.launch {
                    tagsImpl.postValue(repository.getTags(siteId, tagsSearch.value, 1))
                    loadedTagsFromSite = siteId
                    loadedTagsFilteredBy = tagsSearch.value
                }
            }
        }
    }

    fun closeSites() = shouldShowSitesImpl.postValue(false)
}
