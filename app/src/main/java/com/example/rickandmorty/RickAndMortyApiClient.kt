package com.example.rickandmorty

import com.example.rickandmorty.RickAndMortyApp.Companion.BASE_URL

class RickAndMortyApiClient {
    private val retrofitService: RickAndMortyApiService = RetrofitClient.getClient(BASE_URL).create(RickAndMortyApiService::class.java)
        //функция для вызова апи с использованием ретрофита
    suspend fun getCharacters(): Results {
        return retrofitService.getCharacters()
    }
}
