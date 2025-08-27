package com.example.mahjongscroeboard.data.db

import androidx.room.ColumnInfo

data class PlayerStats(
    @ColumnInfo(name = "playerId")
    val playerId: Int,

    @ColumnInfo(name = "name")
    val playerName: String,

    @ColumnInfo(name = "tsumo_count")
    val tsumoCount: Int,

    @ColumnInfo(name = "ron_count")
    val ronCount: Int,

    @ColumnInfo(name = "total_wins")
    val totalWins: Int
)
