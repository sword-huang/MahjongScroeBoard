package com.example.mahjongscroeboard.db

data class PlayerStats(
    val playerName: String,
    val totalScore: Int,
    val gamesPlayed: Int,
    val wins: Int
)