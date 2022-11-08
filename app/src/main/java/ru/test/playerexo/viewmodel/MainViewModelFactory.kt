package ru.test.playerexo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.test.playerexo.repository.MainRepository

class MainViewModelFactory(
    private val repository: MainRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(MainRepository::class.java)
            .newInstance(repository)
    }
}
