package dev.vitorramos.livequestions.model

import android.content.SharedPreferences
import dev.vitorramos.livequestions.getString
import dev.vitorramos.livequestions.string
import org.koin.java.KoinJavaComponent.inject

interface Repository {
    suspend fun getQuestions(siteId: String, tag: String?, page: Int): List<Question>
    suspend fun getTags(siteId: String, inname: String?, page: Int): List<String>
    suspend fun getSites(): List<SiteData>
    fun getSelectedSiteId(): String
    suspend fun selectSite(siteId: String)
    suspend fun selectTag(tag: String?)
    fun getSelectedTag(): String
    suspend fun getSite(siteId: String): Site?
}

class RepositoryImpl : Repository {
    private val database by inject(AppDatabase::class.java)
    private val service by inject(Service::class.java)
    private val sharedPref by inject(SharedPreferences::class.java)

    override suspend fun getQuestions(siteId: String, tag: String?, page: Int): List<Question> {
        val response = service.fetchQuestions(page, tag, siteId)
        return response?.body()?.items?.filterNotNull()?.map { Question(it) } ?: listOf()
    }

    override suspend fun getTags(siteId: String, inname: String?, page: Int) =
            service.fetchTags(siteId, page, inname)?.body()?.items?.filterNotNull()?.map { it.name }
                    ?: listOf()

    override suspend fun getSites() = mutableListOf<SiteData>().apply {
        addAll(database.siteDao().getSites())
        if (isEmpty()) service.fetchSites()?.body()?.items?.filterNotNull()?.let {
            addAll(it)
            database.siteDao().putSites(it)
        }
    }

    override fun getSelectedSiteId() =
            sharedPref.string(SHARED_PREF_KEY_SELECTED_SITE, "")

    override suspend fun selectSite(siteId: String) {
        sharedPref.edit().apply {
            putString(SHARED_PREF_KEY_SELECTED_SITE, siteId)
            apply()
        }
    }

    override fun getSelectedTag() = sharedPref.getString(SHARED_PREF_KEY_SELECTED_TAG) ?: ""

    override suspend fun selectTag(tag: String?) {
        sharedPref.edit().apply {
            putString(SHARED_PREF_KEY_SELECTED_TAG, tag)
            apply()
        }
    }

    override suspend fun getSite(siteId: String) = database.siteDao().getSite(siteId)
}

private const val SHARED_PREF_KEY_SELECTED_SITE = "SHARED_PREF_KEY_SELECTED_SITE"
private const val SHARED_PREF_KEY_SELECTED_TAG = "SHARED_PREF_KEY_SELECTED_TAG"
