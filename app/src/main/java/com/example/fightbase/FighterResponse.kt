package com.example.fightbase
import com.example.fightbase.R


data class FighterResponse(
    val id: Int,
    val name: String,
    val weightClass: String,
    val nationality: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val fighterImage: String
)
