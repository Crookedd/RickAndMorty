package com.example.rickandmorty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterViewModel(application: Application) : AndroidViewModel(application) {
    private val apiClient = RickAndMortyApiClient()
    private val charactersLiveData = MutableLiveData<Results>()

    fun getCharacters(): LiveData<Results> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val characters = apiClient.getCharacters()
                charactersLiveData.postValue(characters)
            } catch (e: Exception) {
            }
        }
        return charactersLiveData
    }
}
