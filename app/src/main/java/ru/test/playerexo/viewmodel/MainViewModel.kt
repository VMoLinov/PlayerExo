package ru.test.playerexo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.test.playerexo.model.ui.ChannelUI
import ru.test.playerexo.repository.MainRepository

class MainViewModel(
    application: Application,
    private val repository: MainRepository
) : AndroidViewModel(application) {

    val allData = repository.data
    val favData = repository.favorites
    val finderData = MutableLiveData<List<ChannelUI>?>()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            repository.getData()
        }
    }

    fun updateFavorite(channel: ChannelUI) {
        viewModelScope.launch {
            channel.isFavorite = !channel.isFavorite
            repository.update(channel)
        }
    }

    fun finderRefresh(string: String) {
        if (string.isNotEmpty()) {
            finderData.postValue(allData.value?.filter {
                it.nameRu.contains(string, false)
            })
        } else finderData.postValue(null)
    }
}
