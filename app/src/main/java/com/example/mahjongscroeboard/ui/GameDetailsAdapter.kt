package com.example.mahjongscroeboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mahjongscroeboard.R
import com.example.mahjongscroeboard.data.db.GameRecord
import java.text.SimpleDateFormat
import java.util.*

class GameDetailsAdapter(private val records: List<GameRecord>) : RecyclerView.Adapter<GameDetailsAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.gameDate.text = dateFormat.format(Date(record.date))
        holder.winType.text = record.winType
        holder.score.text = "${record.score}點"
    }

    override fun getItemCount() = records.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameDate: TextView = itemView.findViewById(R.id.gameDate)
        val winType: TextView = itemView.findViewById(R.id.winType)
        val score: TextView = itemView.findViewById(R.id.score)
    }
}
