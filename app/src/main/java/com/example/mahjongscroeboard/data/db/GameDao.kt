package com.example.mahjongscroeboard.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayer(player: Player): Long

    @Query("SELECT * FROM players WHERE name = :name LIMIT 1")
    suspend fun getPlayerByName(name: String): Player?

    @Insert
    suspend fun insertGameRecord(gameRecord: GameRecord)

    @Query("""
        SELECT 
            p.id as playerId,
            p.name, 
            COUNT(CASE WHEN gr.winType = 'TSUMO' THEN 1 END) as tsumo_count,
            COUNT(CASE WHEN gr.winType = 'RON' THEN 1 END) as ron_count,
            COUNT(gr.id) as total_wins
        FROM 
            players p
        LEFT JOIN 
            game_records gr ON p.id = gr.playerId
        GROUP BY 
            p.id
        ORDER BY 
            total_wins DESC
    """)
    fun getPlayerStats(): Flow<List<PlayerStats>>

    @Query("SELECT * FROM game_records WHERE playerId = :playerId ORDER BY date DESC")
    fun getGameRecordsForPlayer(playerId: Int): Flow<List<GameRecord>>

}
