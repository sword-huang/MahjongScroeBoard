package com.example.mahjongscroeboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mahjongscroeboard.R
import com.example.mahjongscroeboard.data.db.PlayerStats

class PlayerStatsAdapter(
    private val players: List<PlayerStats>,
    private val onItemClicked: (PlayerStats) -> Unit
) : RecyclerView.Adapter<PlayerStatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_player_stats_ui, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        holder.playerName.text = player.playerName
        holder.totalWins.text = "總胡: ${player.totalWins}"
        holder.tsumoCount.text = "自摸: ${player.tsumoCount}"
        holder.ronCount.text = "放槍胡: ${player.ronCount}"
        holder.itemView.setOnClickListener { onItemClicked(player) }
    }

    override fun getItemCount() = players.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerName: TextView = itemView.findViewById(R.id.playerName)
        val totalWins: TextView = itemView.findViewById(R.id.totalWins)
        val tsumoCount: TextView = itemView.findViewById(R.id.tsumoCount)
        val ronCount: TextView = itemView.findViewById(R.id.ronCount)
    }
}
