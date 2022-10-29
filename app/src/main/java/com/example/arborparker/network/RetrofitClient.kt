package com.example.arborparker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://arbor-parker-heroku.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkApi::class.java)
    }

}