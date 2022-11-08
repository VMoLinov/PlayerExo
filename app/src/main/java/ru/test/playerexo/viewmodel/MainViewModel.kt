package ru.test.playerexo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.test.playerexo.model.ui.ChannelUI
import ru.test.playerexo.repository.MainRepository

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val allData = repository.data
    val favData = repository.favorites
    val finderData = MutableLiveData<List<ChannelUI>?>()

    init {
        getData()
    }

    private fun getData() {
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
                it.nameRu.contains(string, true)
            })
        } else finderData.postValue(null)
    }
}
