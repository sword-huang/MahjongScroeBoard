package com.example.mahjongscroeboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val player1Name: String,
    val player1Score: Int,
    val player2Name: String,
    val player2Score: Int,
    val player3Name: String,
    val player3Score: Int,
    val player4Name: String,
    val player4Score: Int
)