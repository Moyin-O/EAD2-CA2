package com.example.fightbase

data class FightRecordResponse(
    val id: Int,
    val fighterId: Int,
    val opponentId: Int,
    val eventName: String,
    val result: String,
    val date: String,
    val fighter: FighterResponse,
    val opponent: FighterResponse
)
