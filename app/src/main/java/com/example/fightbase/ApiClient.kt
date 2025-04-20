package com.example.fightbase

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ead2.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val fighterService: FighterApiService = retrofit.create(FighterApiService::class.java)
}
