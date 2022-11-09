package ru.test.playerexo.di

import dagger.Binds
import dagger.Module
import ru.test.playerexo.repository.MainRepository
import ru.test.playerexo.repository.MainRepositoryImpl

@Module
interface AppBinds {

    @Binds
    fun bind(mainRepositoryImpl: MainRepositoryImpl): MainRepository
}
