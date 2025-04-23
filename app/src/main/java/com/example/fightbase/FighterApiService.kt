package com.example.fightbase

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.Response

interface FighterApiService {

    @GET("api/fighters")
    suspend fun getFighters(): List<FighterResponse>

    @POST("api/fighters")
    suspend fun addFighter(@Body fighter: FighterRequest): Response<Unit>

    @DELETE("api/fighters/{name}")
    suspend fun deleteFighter(@Path("name") name: String): Response<Unit>
}
