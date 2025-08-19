package com.example.mahjongscroeboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mahjongscroeboard.db.PlayerStats

class PlayerStatsAdapter(private val onClick: (PlayerStats) -> Unit) :
    ListAdapter<PlayerStats, PlayerStatsAdapter.PlayerStatsViewHolder>(PlayerStatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerStatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_player_stats, parent, false)
        return PlayerStatsViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: PlayerStatsViewHolder, position: Int) {
        val stats = getItem(position)
        holder.bind(stats)
    }

    class PlayerStatsViewHolder(itemView: View, val onClick: (PlayerStats) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val playerNameText: TextView = itemView.findViewById(R.id.player_name_text)
        private val totalScoreText: TextView = itemView.findViewById(R.id.total_score_text)
        private val gamesPlayedText: TextView = itemView.findViewById(R.id.games_played_text)
        private val winsText: TextView = itemView.findViewById(R.id.wins_text)
        private var currentPlayerStats: PlayerStats? = null

        init {
            itemView.setOnClickListener {
                currentPlayerStats?.let {
                    onClick(it)
                }
            }
        }

        fun bind(stats: PlayerStats) {
            currentPlayerStats = stats
            playerNameText.text = stats.playerName
            totalScoreText.text = "總分: ${stats.totalScore}"
            gamesPlayedText.text = "場次: ${stats.gamesPlayed}"
            winsText.text = "勝利: ${stats.wins}"
        }
    }
}

class PlayerStatsDiffCallback : DiffUtil.ItemCallback<PlayerStats>() {
    override fun areItemsTheSame(oldItem: PlayerStats, newItem: PlayerStats): Boolean {
        return oldItem.playerName == newItem.playerName
    }

    override fun areContentsTheSame(oldItem: PlayerStats, newItem: PlayerStats): Boolean {
        return oldItem == newItem
    }
}