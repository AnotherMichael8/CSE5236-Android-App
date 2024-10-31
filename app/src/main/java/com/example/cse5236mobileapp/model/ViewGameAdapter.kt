package com.example.cse5236mobileapp.model

import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R

class ViewGameAdapter (
    private val games: MutableList<Game>
) : RecyclerView.Adapter<ViewGameAdapter.GameViewHolder>(){

    class GameViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvPlayerOneScore : TextView = itemView.findViewById(R.id.tvPlayerOneScore)
        val tvPlayerTwoScore : TextView = itemView.findViewById(R.id.tvPlayerTwoScore)
        val tvPlayerOne : TextView = itemView.findViewById(R.id.tvPlayerOne)
        val tvPlayerTwo: TextView = itemView.findViewById(R.id.tvPlayerTwo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game_vs_component, parent, false)
        )
    }
    fun addGame(game: Game)
    {
        games.add(game)
        notifyItemInserted(games.size - 1)
    }
    fun deleteGame(game: Game)
    {
        games.remove(game)
    }
    fun updateGames(updatedGames : List<Game>)
    {
        games.removeAll(games)
        games.addAll(updatedGames)
        Log.d("GameAdapter", "Successful Update")
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val curGame = games[position]
        holder.apply{
            tvPlayerOneScore.text = curGame.teamOneScore.toString();
            tvPlayerTwoScore.text = curGame.teamTwoScore.toString();
            tvPlayerOne.text = curGame.teamOne
            tvPlayerTwo.text = curGame.teamTwo
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}
