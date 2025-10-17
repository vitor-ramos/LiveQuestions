package dev.vitorramos.livequestions.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("2.2/questions?order=desc&sort=creation&pageSize=30")
    suspend fun fetchQuestions(
        @Query("page") page: Int,
        @Query("tagged") tagged: String?,
        @Query("site") site: String,
    ): Response<QuestionsResponse?>?

    @GET("2.2/tags?&pagesize=100&order=desc&sort=popular")
    suspend fun fetchTags(
        @Query("site") site: String,
        @Query("page") page: Int = 1,
        @Query("inname") inname: String? = null,
    ): Response<TagsResponse?>?

    @GET("2.2/sites?page=1&pageSize=1000")
    suspend fun fetchSites(): Response<SitesResponse?>?
}
