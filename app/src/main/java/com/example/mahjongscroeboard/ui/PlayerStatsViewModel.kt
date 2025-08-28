package com.example.mahjongscroeboard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mahjongscroeboard.data.db.AppDatabase
import com.example.mahjongscroeboard.data.db.PlayerStats
import kotlinx.coroutines.launch

class PlayerStatsViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = AppDatabase.getDatabase(application).gameDao()

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
}

enum class SortColumn {
    PLAYER_NAME,
    TSUMO_COUNT,
    RON_COUNT,
    TOTAL_WINS
}
