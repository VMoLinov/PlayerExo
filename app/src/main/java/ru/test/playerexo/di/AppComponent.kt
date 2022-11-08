package ru.test.playerexo.di

import dagger.Component
import ru.test.playerexo.ui.main.MainFragment
import ru.test.playerexo.ui.main.viewpager.ViewPagerItem
import ru.test.playerexo.viewmodel.MainViewModelFactory
import javax.inject.Singleton

@Component(modules = [MainModule::class])
@Singleton
interface AppComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(viewPagerItem: ViewPagerItem)

    fun viewModelFactory(): MainViewModelFactory
}
