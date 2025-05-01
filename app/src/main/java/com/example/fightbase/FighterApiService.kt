package com.example.fightbase

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.Response

interface FighterApiService {
    @GET("api/fighters")
    suspend fun getFighters(): List<FighterResponse>

    @POST("api/fighters")
    suspend fun addFighter(@Body fighter: FighterRequest): Response<Unit>

    @PUT("api/fighters/{id}")
    suspend fun updateFighter(
        @Path("id") id: Int,
        @Body fighter: FighterRequest
    ): Response<Unit>

    @DELETE("api/fighters/{id}")
    suspend fun deleteFighter(@Path("id") id: Int): Response<Unit>

    @GET("api/fightrecords")
    suspend fun getFights(): List<FightRecordResponse>

    @POST("api/fightrecords")
    suspend fun addFight(@Body fight: FightRecordRequest)

    @DELETE("api/fightrecords/{id}")
    suspend fun deleteFight(@Path("id") id: Int): Response<Unit>
}
