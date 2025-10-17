package dev.vitorramos.livequestions.viewmodel

import androidx.lifecycle.*
import dev.vitorramos.livequestions.composables.SheetContentEvents
import dev.vitorramos.livequestions.composables.SitesListContentEvents
import dev.vitorramos.livequestions.model.*
import dev.vitorramos.livequestions.moveToFirst
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel(), SitesListContentEvents, SheetContentEvents {
    private val repository: Repository by inject(Repository::class.java)

    private val questionsImpl = MediatorLiveData<List<Question>>()
    private val sitesImpl = MediatorLiveData<List<SiteData>>()
    private val tagsImpl = MediatorLiveData<List<String>>()

    private val siteImpl = MutableLiveData<Site>()
    private val tagImpl = MutableLiveData(repository.getSelectedTag())

    private val sitesSearchImpl = MutableLiveData("")
    private val tagsSearchImpl = MutableLiveData("")

    private val loadingImpl = MutableLiveData<Boolean>()

    val questions: LiveData<List<Question>> = questionsImpl
    val sites: LiveData<List<SiteData>> = sitesImpl
    val tags: LiveData<List<String>> = tagsImpl

    val site: LiveData<Site> = siteImpl
    val tag: LiveData<String> = tagImpl

    val sitesSearch: LiveData<String> = sitesSearchImpl
    val tagsSearch: LiveData<String> = tagsSearchImpl

    val loading: LiveData<Boolean> = loadingImpl

    // cache controllers - start
    private var loadedQuestionsFromSite: String? = null
    private var loadedQuestionsWithTag: String? = null

    private var loadedTagsFromSite: String? = null
    private var loadedTagsFilteredBy: String? = null

    private var sitesFilteredBySearch: String? = null
    // cache controllers - end

    private var allSites = listOf<SiteData>()
    private var pageLoaded = 1

    init {
        questionsImpl.addSource(site) { site ->
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
        questionsImpl.addSource(tag) { tag ->
            if (tag != loadedQuestionsWithTag) {
                viewModelScope.launch {
                    pageLoaded = 1
                    (site.value as? SiteData)?.let {
                        val list = repository.getQuestions(it.apiSiteParameter, tag, 1)
                        loadedQuestionsFromSite = it.apiSiteParameter
                        loadedQuestionsWithTag = tag
                        questionsImpl.postValue(list)
                    }
                }
            }
        }
        tagsImpl.addSource(site) {
            (it as? SiteData)?.apiSiteParameter?.let { siteId ->
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
                val filtered = allSites.filter { it.name.contains(search, true) }
                val movedSelectedToFirst = (site.value as? SiteData)?.let { siteData ->
                    filtered.moveToFirst { it.apiSiteParameter == siteData.apiSiteParameter }
                } ?: filtered
                sitesImpl.postValue(movedSelectedToFirst)
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
            val movedSelectedToFirst = (site.value as? SiteData)?.let { siteData ->
                allSites.moveToFirst { it.apiSiteParameter == siteData.apiSiteParameter }
            } ?: allSites
            sitesImpl.postValue(movedSelectedToFirst)
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
        }
    }

    override fun onClickTagSearch() {
        (site.value as? SiteData)?.apiSiteParameter?.let { siteId ->
            if (loadedTagsFilteredBy != tagsSearch.value) {
                viewModelScope.launch {
                    tagsImpl.postValue(repository.getTags(siteId, tagsSearch.value, 1))
                    loadedTagsFromSite = siteId
                    loadedTagsFilteredBy = tagsSearch.value
                }
            }
        }
    }
}
