package com.example.rickandmorty

import android.app.Application

class RickAndMortyApp : Application() {
    companion object { //добавила объект компаньён для базовой ссылки, чтобы была возможность потом иметь доступ
        //к этому объекту в любой точки кода.
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

}
