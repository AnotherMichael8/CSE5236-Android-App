package com.example.cse5236mobileapp.model

import android.nfc.Tag
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel

class ViewGameAdapter (
    private val tournamentIdentifier: TournamentIdentifier,
    private val numRounds: Int
) : RecyclerView.Adapter<ViewGameAdapter.GameViewHolder>(){

    private var eachRoundGames : Array<MutableList<Game>> = Array(numRounds) { mutableListOf() }
    private var currentRound = 0
    private val tournamentGamesViewModel = TournamentGamesViewModel(tournamentIdentifier.tournamentId)
    private var players = listOf<String>()
    private var playerMap = mapOf<String, String>()


    class GameViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvPlayerOneScore : TextView = itemView.findViewById(R.id.tvPlayerOneScore)
        val tvPlayerTwoScore : TextView = itemView.findViewById(R.id.tvPlayerTwoScore)
        val tvPlayerOne : TextView = itemView.findViewById(R.id.tvPlayerOne)
        val tvPlayerTwo: TextView = itemView.findViewById(R.id.tvPlayerTwo)
        var tvRound: TextView = itemView.findViewById(R.id.txtRoundNumber)
        val tvGameProgress : TextView = itemView.findViewById(R.id.tvGameProgress)
        val etPlayerOneScore : EditText = itemView.findViewById(R.id.etPlayerOneScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game_vs_component, parent, false)
        )
    }
    fun updateGames(updatedGames : List<Game>)
    {
        if(eachRoundGames[currentRound].isEmpty())
        {
            primeTournament(updatedGames)
        }
        else {
            var roundItemCnt = 0
            for (i in updatedGames.indices) {
                //val test1 = updatedGames[i].round == currentRound + 1
                //val test2 = !(eachRoundGames[currentRound][roundItemCnt].equals(updatedGames[i]))
                if (updatedGames[i].round == currentRound + 1) {
                    if(!(eachRoundGames[currentRound][roundItemCnt].equals(updatedGames[i]))) {
                        if (updatedGames[i].gameStatus == "Final") {
                            addNextRoundGame(roundItemCnt, updatedGames[i])
                        }
                        eachRoundGames[currentRound][roundItemCnt] = updatedGames[i]
                        notifyItemChanged(roundItemCnt)
                    }
                    roundItemCnt++
                }
            }
        }
        Log.d("GameAdapter", "Successful Update")
    }
    private fun primeTournament(updatedGames: List<Game>)
    {
        for(i in 0..< numRounds)
        {
            repeat(Math.pow(2.0, i.toDouble()).toInt())
            {
                eachRoundGames[numRounds - 1 - i].add(Game(round = numRounds - i))
            }
        }
        for(i in updatedGames.indices)
        {
            eachRoundGames[updatedGames[i].round - 1].set(updatedGames[i].gamePosition, updatedGames[i])
        }
        notifyDataSetChanged()
    }
    private fun addNextRoundGame(position: Int, game: Game)
    {
        if(currentRound < numRounds - 1) {
            var advancedPlayer = ""
            if(game.teamOneScore > game.teamTwoScore)
            {
                advancedPlayer = game.teamOne
            }
            else
            {
                advancedPlayer = game.teamTwo
            }

            val gameObject = eachRoundGames[currentRound + 1][position / 2]
            val oldGame = Pair<String, String>(gameObject.teamOne, gameObject.teamTwo)
            if(position % 2 == 0) {
                gameObject.teamOne = advancedPlayer
            }
            else {
                gameObject.teamTwo = advancedPlayer
            }
            gameObject.gamePosition = position/2
            tournamentGamesViewModel.updateOldGameToNewGameDatabase(oldGame, eachRoundGames[currentRound + 1][position / 2])
        }
    }
    fun nextRound() : Int
    {
        if(currentRound < numRounds - 1) {
            currentRound++
        }
        notifyDataSetChanged()
        return currentRound + 1
    }
    fun previousRound() : Int
    {
        if(currentRound > 0){
            currentRound--
        }
        notifyDataSetChanged()
        return currentRound + 1
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val curGame = eachRoundGames[currentRound][position]
        holder.apply{
            tvPlayerOneScore.text = curGame.teamOneScore.toString()
            etPlayerOneScore.setText(curGame.teamOneScore.toString())
            tvPlayerTwoScore.text = curGame.teamTwoScore.toString()
            tvPlayerOne.text = linkPlayerName(curGame.teamOne)
            tvPlayerTwo.text = linkPlayerName(curGame.teamTwo)
            tvGameProgress.text = curGame.gameStatus
            tvPlayerOneScore.visibility = View.INVISIBLE
            tvRound.text = curGame.getRoundName(numRounds)

            etPlayerOneScore.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Handle Enter key press here
                    val newScore = etPlayerOneScore.text.toString().trim().toIntOrNull()
                    if(newScore != null) {
                        curGame.teamOneScore = newScore
                        tournamentGamesViewModel.updateOldGameToNewGameDatabase(Pair(curGame.teamOne, curGame.teamTwo), curGame)
                        notifyItemChanged(position)
                    }
                    else
                    {
                        Log.i("Adapter", "Invalid input for score")
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return eachRoundGames[currentRound].size
    }

    private fun linkPlayerName(previousName: String): String {
        // Converting previous name to int
        val previousInt = previousName.toIntOrNull()
        if (previousInt != null) {
            if (previousInt < players.size) {
                return playerMap[players[previousInt]] ?: previousName
            }
            else {
                return "Placeholder$previousName"
            }
        }
        else {
            return playerMap[previousName] ?: previousName
        }
    }

    fun updatePlayerList(updatedPlayerList: List<String>) {
        players = updatedPlayerList
    }

    fun updatePlayerMap(updatedPlayerMap: Map<String, String>) {
        playerMap = updatedPlayerMap

        notifyDataSetChanged()
    }
}
