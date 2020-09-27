package dev.vitorramos.livequestions.model

import androidx.room.*

@Database(entities = [SiteData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun siteDao(): SiteDao
}

@Dao
interface SiteDao {
    @Query("SELECT * from siteData")
    suspend fun getSites(): List<SiteData>

    @Insert
    suspend fun putSites(sites: List<SiteData>)

    @Query("SELECT * from siteData WHERE apiSiteParameter=:api_site_parameter")
    suspend fun getSite(api_site_parameter: String): SiteData?
}
