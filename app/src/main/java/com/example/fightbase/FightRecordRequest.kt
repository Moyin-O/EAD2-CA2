package com.example.fightbase

data class FightRecordRequest(
    val fighterId: Int,
    val opponentId: Int,
    val eventName: String,
    val result: String,
    val date: String
)
