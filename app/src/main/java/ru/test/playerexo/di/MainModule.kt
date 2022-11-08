package ru.test.playerexo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.test.playerexo.app.App
import ru.test.playerexo.repository.MainRepository
import ru.test.playerexo.repository.MainRepositoryImpl
import ru.test.playerexo.repository.local.LocalDatabase
import ru.test.playerexo.repository.local.LocalSource
import ru.test.playerexo.repository.network.NetworkImpl
import ru.test.playerexo.repository.network.NetworkSource
import ru.test.playerexo.viewmodel.MainViewModelFactory
import javax.inject.Singleton

@Module
class MainModule(val app: App) {

    @Provides
    @Singleton
    fun factory(repository: MainRepository): MainViewModelFactory {
        return MainViewModelFactory(repository)
    }

    @Provides
    @Singleton
    fun context(): Context = app

    @Provides
    @Singleton
    fun mainRepository(localSource: LocalSource, networkSource: NetworkSource): MainRepository =
        MainRepositoryImpl(localSource, networkSource)

    @Provides
    @Singleton
    fun localSource(context: Context): LocalSource {
        return Room.databaseBuilder(
            context, LocalDatabase::class.java, DB_NAME
        ).fallbackToDestructiveMigration().build().localSource()
    }

    @Provides
    @Singleton
    fun networkSource(): NetworkSource {
        return NetworkImpl(
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetworkSource::class.java)
        )
    }

    companion object {
        const val DB_NAME = "channels"
        const val BASE_URL = "https://limehd.online/playlist/"
    }
}
