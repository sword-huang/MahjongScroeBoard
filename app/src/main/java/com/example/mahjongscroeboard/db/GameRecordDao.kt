package com.example.mahjongscroeboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Insert
    suspend fun insert(gameRecord: GameRecord)

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<GameRecord>>

    @Query("""
        SELECT playerName, SUM(score) as totalScore, COUNT(playerName) as gamesPlayed, SUM(CASE WHEN score > 0 THEN 1 ELSE 0 END) as wins
        FROM (
            SELECT player1Name as playerName, player1Score as score FROM game_records
            UNION ALL
            SELECT player2Name as playerName, player2Score as score FROM game_records
            UNION ALL
            SELECT player3Name as playerName, player3Score as score FROM game_records
            UNION ALL
            SELECT player4Name as playerName, player4Score as score FROM game_records
        )
        GROUP BY playerName
        ORDER BY totalScore DESC
    """)
    fun getPlayerStats(): Flow<List<PlayerStats>>

    @Query("SELECT * FROM game_records WHERE player1Name = :playerName OR player2Name = :playerName OR player3Name = :playerName OR player4Name = :playerName ORDER BY timestamp DESC")
    fun getRecordsForPlayer(playerName: String): Flow<List<GameRecord>>
}