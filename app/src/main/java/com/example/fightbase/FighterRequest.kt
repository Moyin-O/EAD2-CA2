package com.example.fightbase

data class FighterRequest(
    val name: String,
    val weightClass: String,
    val nationality: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val fighterImage: String
)
