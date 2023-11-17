package com.example.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nework.auth.AppAuth
import com.example.nework.dto.Post
import com.example.nework.model.StateModel
import com.example.nework.repository.WallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WallViewModel @Inject constructor(
    private val repository: WallRepository,
    private val auth: AppAuth,
) : ViewModel() {

    val data: LiveData<List<Post>> =
        repository.data
            .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel>
        get() = _dataState

    fun loadUserWall(userId: Long) = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            repository.getUserWall(userId)
            _dataState.postValue(StateModel())
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

}