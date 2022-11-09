package ru.test.playerexo.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.test.playerexo.ui.main.MainFragment
import ru.test.playerexo.ui.main.viewpager.ViewPagerItem
import ru.test.playerexo.viewmodel.MainViewModelFactory
import javax.inject.Singleton

@Component(modules = [MainModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(mainFragment: MainFragment)
    fun inject(viewPagerItem: ViewPagerItem)

    fun viewModelFactory(): MainViewModelFactory
}
