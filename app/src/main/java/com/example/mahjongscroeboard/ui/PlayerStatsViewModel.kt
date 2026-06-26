package com.example.mahjongscroeboard.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mahjongscroeboard.data.db.AppDatabase
import com.example.mahjongscroeboard.data.db.GameRecord
import com.example.mahjongscroeboard.data.db.Player
import com.example.mahjongscroeboard.data.db.PlayerStats
import androidx.room.withTransaction
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class PlayerStatsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val gameDao = database.gameDao()

    private val _playerStats = gameDao.getPlayerStats().asLiveData()
    val playerStats: LiveData<List<PlayerStats>> = _playerStats

    private var currentSortColumn: SortColumn? = null
    private var isAscending = true

    fun sort(sortColumn: SortColumn) {
        val stats = _playerStats.value ?: return

        if (currentSortColumn == sortColumn) {
            isAscending = !isAscending
        } else {
            currentSortColumn = sortColumn
            isAscending = true
        }

        val sortedStats = when (sortColumn) {
            SortColumn.PLAYER_NAME -> if (isAscending) stats.sortedBy { it.playerName } else stats.sortedByDescending { it.playerName }
            SortColumn.TSUMO_COUNT -> if (isAscending) stats.sortedBy { it.tsumoCount } else stats.sortedByDescending { it.tsumoCount }
            SortColumn.RON_COUNT -> if (isAscending) stats.sortedBy { it.ronCount } else stats.sortedByDescending { it.ronCount }
            SortColumn.TOTAL_WINS -> if (isAscending) stats.sortedBy { it.totalWins } else stats.sortedByDescending { it.totalWins }
        }

        (playerStats as MutableLiveData).value = sortedStats
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            gameDao.clearAllGameRecords()
            gameDao.clearAllPlayers()
        }
    }

    suspend fun exportBackup(uri: Uri) = withContext(Dispatchers.IO) {
        val players = gameDao.getAllPlayers()
        val gameRecords = gameDao.getAllGameRecords()

        val playersJson = JSONArray().apply {
            players.forEach { player ->
                put(
                    JSONObject()
                        .put("id", player.id)
                        .put("name", player.name)
                )
            }
        }

        val recordsJson = JSONArray().apply {
            gameRecords.forEach { record ->
                put(
                    JSONObject()
                        .put("id", record.id)
                        .put("playerId", record.playerId)
                        .put("winType", record.winType)
                        .put("score", record.score)
                        .put("date", record.date)
                )
            }
        }

        val backup = JSONObject()
            .put("formatVersion", 1)
            .put("exportedAt", System.currentTimeMillis())
            .put("players", playersJson)
            .put("gameRecords", recordsJson)

        val resolver = getApplication<Application>().contentResolver
        resolver.openOutputStream(uri)?.use { output ->
            output.writer(Charsets.UTF_8).use { writer ->
                writer.write(backup.toString(2))
            }
        } ?: throw IllegalStateException("無法建立備份檔案")
    }

    suspend fun importBackup(uri: Uri) = withContext(Dispatchers.IO) {
        val resolver = getApplication<Application>().contentResolver
        val jsonText = resolver.openInputStream(uri)?.use { input ->
            input.bufferedReader(Charsets.UTF_8).readText()
        } ?: throw IllegalStateException("無法讀取備份檔案")

        val root = JSONObject(jsonText)
        val playersArray = root.optJSONArray("players")
            ?: throw IllegalArgumentException("備份檔格式錯誤：缺少 players")
        val recordsArray = root.optJSONArray("gameRecords")
            ?: throw IllegalArgumentException("備份檔格式錯誤：缺少 gameRecords")

        val players = mutableListOf<Player>()
        for (i in 0 until playersArray.length()) {
            val item = playersArray.getJSONObject(i)
            players.add(
                Player(
                    id = item.getInt("id"),
                    name = item.getString("name")
                )
            )
        }

        val gameRecords = mutableListOf<GameRecord>()
        for (i in 0 until recordsArray.length()) {
            val item = recordsArray.getJSONObject(i)
            gameRecords.add(
                GameRecord(
                    id = item.getInt("id"),
                    playerId = item.getInt("playerId"),
                    winType = item.getString("winType"),
                    score = item.getInt("score"),
                    date = item.optLong("date", System.currentTimeMillis())
                )
            )
        }

        database.withTransaction {
            gameDao.clearAllGameRecords()
            gameDao.clearAllPlayers()
            if (players.isNotEmpty()) {
                gameDao.insertPlayers(players)
            }
            if (gameRecords.isNotEmpty()) {
                gameDao.insertGameRecords(gameRecords)
            }
        }
    }
}

enum class SortColumn {
    PLAYER_NAME,
    TSUMO_COUNT,
    RON_COUNT,
    TOTAL_WINS
}
