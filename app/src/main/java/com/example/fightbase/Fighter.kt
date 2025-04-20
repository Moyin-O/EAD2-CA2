package com.example.fightbase

data class FighterApiResponse(
    val documents: List<FighterDocument>
)

data class FighterDocument(
    val name: String,
    val fields: FighterFields
)

data class FighterFields(
    val name: FirestoreString,
    val wins: FirestoreInt,
    val losses: FirestoreInt,
    val draws: FirestoreInt,
    val nationality: FirestoreString,
    val weightClass: FirestoreString,
    val fighterImage: FirestoreString
)



data class FirestoreString(val stringValue: String)
data class FirestoreInt(val integerValue: String)
