package com.example.mahjongscroeboard.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.mahjongscroeboard.data.db.AppDatabase

class GameDetailsViewModel(application: Application, playerId: Int) : ViewModel() {

    private val gameDao = AppDatabase.getDatabase(application).gameDao()

    val gameRecords = gameDao.getGameRecordsForPlayer(playerId).asLiveData()
}

class GameDetailsViewModelFactory(private val application: Application, private val playerId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameDetailsViewModel(application, playerId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
