package dev.vitorramos.livequestions

import android.app.Application
import androidx.room.Room
import dev.vitorramos.livequestions.model.AppDatabase
import dev.vitorramos.livequestions.model.Repository
import dev.vitorramos.livequestions.model.RepositoryImpl
import dev.vitorramos.livequestions.model.Service
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("unused") // used in manifest
class LiveQuestionsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(module {
                single<Repository> { RepositoryImpl() }
                single<Service> {
                    Retrofit.Builder()
                        .baseUrl("https://api.stackexchange.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(Service::class.java)
                }
                single {
                    Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "live-questions-db"
                    ).build()
                }
                single {
                    getSharedPreferences("preferences", MODE_PRIVATE)
                }
            })
        }
    }
}
