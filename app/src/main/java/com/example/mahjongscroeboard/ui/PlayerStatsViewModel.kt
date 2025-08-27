package com.example.mahjongscroeboard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.mahjongscroeboard.data.db.AppDatabase
import com.example.mahjongscroeboard.data.db.PlayerStats

class PlayerStatsViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = AppDatabase.getDatabase(application).gameDao()

    val playerStats: LiveData<List<PlayerStats>> = gameDao.getPlayerStats().asLiveData()
}
