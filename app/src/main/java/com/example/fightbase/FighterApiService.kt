package com.example.fightbase
import com.example.fightbase.FighterRequest


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

interface FighterApiService {

    @GET("api/fighters")
    suspend fun getFighters(): List<FighterResponse>

    @POST("api/fighters")
    suspend fun addFighter(@Body fighter: FighterRequest): Response<Unit>
}
