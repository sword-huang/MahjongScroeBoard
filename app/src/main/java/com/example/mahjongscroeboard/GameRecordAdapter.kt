package com.example.mahjongscroeboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mahjongscroeboard.db.GameRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameRecordAdapter : ListAdapter<GameRecord, GameRecordAdapter.GameRecordViewHolder>(GameRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_record, parent, false)
        return GameRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameRecordViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    class GameRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestampText: TextView = itemView.findViewById(R.id.timestamp_text)
        private val player1NameText: TextView = itemView.findViewById(R.id.player1_name_text)
        private val player1ScoreText: TextView = itemView.findViewById(R.id.player1_score_text)
        private val player2NameText: TextView = itemView.findViewById(R.id.player2_name_text)
        private val player2ScoreText: TextView = itemView.findViewById(R.id.player2_score_text)
        private val player3NameText: TextView = itemView.findViewById(R.id.player3_name_text)
        private val player3ScoreText: TextView = itemView.findViewById(R.id.player3_score_text)
        private val player4NameText: TextView = itemView.findViewById(R.id.player4_name_text)
        private val player4ScoreText: TextView = itemView.findViewById(R.id.player4_score_text)

        fun bind(record: GameRecord) {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            timestampText.text = sdf.format(Date(record.timestamp))

            player1NameText.text = record.player1Name
            player1ScoreText.text = record.player1Score.toString()
            player2NameText.text = record.player2Name
            player2ScoreText.text = record.player2Score.toString()
            player3NameText.text = record.player3Name
            player3ScoreText.text = record.player3Score.toString()
            player4NameText.text = record.player4Name
            player4ScoreText.text = record.player4Score.toString()
        }
    }
}

class GameRecordDiffCallback : DiffUtil.ItemCallback<GameRecord>() {
    override fun areItemsTheSame(oldItem: GameRecord, newItem: GameRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameRecord, newItem: GameRecord): Boolean {
        return oldItem == newItem
    }
}