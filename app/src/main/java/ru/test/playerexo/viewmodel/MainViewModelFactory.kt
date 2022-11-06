package ru.test.playerexo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.test.playerexo.repository.MainRepository
import ru.test.playerexo.repository.MainRepositoryImpl

class MainViewModelFactory(
    private val application: Application,
    private val repository: MainRepository = MainRepositoryImpl(application)
) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(
                Application::class.java, MainRepository::class.java
            )
            .newInstance(application, repository)
    }
}
