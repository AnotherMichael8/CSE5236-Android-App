package com.example.cse5236mobileapp.model

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel
import kotlin.math.pow

class ViewGameAdapter (
    private val tournamentIdentifier: TournamentIdentifier,
    private val numRounds: Int
) : RecyclerView.Adapter<ViewGameAdapter.GameViewHolder>(){

    private var eachRoundGames : Array<MutableList<Game>> = Array(numRounds) { mutableListOf() }
    private var currentRound = 0
    private val tournamentGamesViewModel = TournamentGamesViewModel(tournamentIdentifier.tournamentId)
    private var players = listOf<String>()
    private var playerMap = mapOf<String, String>()
    private var privileges = "User"


    class GameViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvPlayerOneScore : TextView = itemView.findViewById(R.id.tvPlayerOneScore)
        val tvPlayerTwoScore : TextView = itemView.findViewById(R.id.tvPlayerTwoScore)
        val tvPlayerOne : TextView = itemView.findViewById(R.id.tvPlayerOne)
        val tvPlayerTwo: TextView = itemView.findViewById(R.id.tvPlayerTwo)
        var tvRound: TextView = itemView.findViewById(R.id.txtRoundNumber)
        val tvGameProgress : TextView = itemView.findViewById(R.id.tvGameProgress)
        val etPlayerOneScore : EditText = itemView.findViewById(R.id.etPlayerOneScore)
        val etPlayerTwoScore : EditText = itemView.findViewById(R.id.etPlayerTwoScore)
        val btFinalizeButton : Button = itemView.findViewById(R.id.btFinalizeGame)
        val btUnfinalizeButton : Button = itemView.findViewById(R.id.btUnfinalizeGame)
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
                    if(eachRoundGames[currentRound][updatedGames[i].gamePosition] != updatedGames[i]) {
                        if (updatedGames[i].gameStatus == "Final" && eachRoundGames[currentRound][updatedGames[i].gamePosition].gameStatus != "Final") {
                            addNextRoundGame(updatedGames[i].gamePosition, updatedGames[i])
                        }
                        eachRoundGames[currentRound][updatedGames[i].gamePosition] = updatedGames[i]
                        notifyItemChanged(updatedGames[i].gamePosition)
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
            repeat(2.0.pow(i.toDouble()).toInt())
            {
                eachRoundGames[numRounds - 1 - i].add(Game(round = numRounds - i))
            }
        }
        for(i in updatedGames.indices)
        {
            eachRoundGames[updatedGames[i].round - 1][updatedGames[i].gamePosition] = updatedGames[i]
        }
        // TODO: Optimize these function calls
        notifyDataSetChanged()
    }
    private fun addNextRoundGame(position: Int, game: Game)
    {
        if(currentRound < numRounds - 1) {
            val advancedPlayer: String = if(game.teamOneScore > game.teamTwoScore) {
                game.teamOne
            } else {
                game.teamTwo
            }

            val gameObject = eachRoundGames[currentRound + 1][position / 2]
            val oldGame = Pair(gameObject.teamOne, gameObject.teamTwo)
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
        // TODO: Optimize these function calls
        notifyDataSetChanged()
        return currentRound + 1
    }
    fun previousRound() : Int
    {
        if(currentRound > 0){
            currentRound--
        }
        // TODO: Optimize these function calls
        notifyDataSetChanged()
        return currentRound + 1
    }
    fun updateUserPrivileges(privileges : String)
    {
        this.privileges = privileges
        // TODO: Optimize these function calls
        notifyDataSetChanged()
    }
    private fun updatePlayerOneScore(curGame: Game, holder: GameViewHolder)
    {
        val newScore = holder.etPlayerOneScore.text.toString().trim().toIntOrNull()
        if (newScore != null) {
            curGame.teamOneScore = newScore
            holder.etPlayerOneScore.clearFocus()
            tournamentGamesViewModel.updateOldGameToNewGameDatabase(
                Pair(curGame.teamOne, curGame.teamTwo), curGame
            )
        } else {
            Log.i("Adapter", "Invalid input for score")
        }
    }
    private fun updatePlayerTwoScore(curGame: Game, holder: GameViewHolder)
    {
        val newScore = holder.etPlayerTwoScore.text.toString().trim().toIntOrNull()
        if (newScore != null) {
            curGame.teamTwoScore = newScore
            holder.etPlayerTwoScore.clearFocus()
            tournamentGamesViewModel.updateOldGameToNewGameDatabase(
                Pair(curGame.teamOne, curGame.teamTwo), curGame
            )
        } else {
            Log.i("Adapter", "Invalid input for score")
        }
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val curGame = eachRoundGames[currentRound][position]
        holder.apply{
            tvPlayerOneScore.text = curGame.teamOneScore.toString()
            etPlayerOneScore.setText(curGame.teamOneScore.toString())
            tvPlayerTwoScore.text = curGame.teamTwoScore.toString()
            etPlayerTwoScore.setText(curGame.teamTwoScore.toString())
            tvPlayerOne.text = linkPlayerName(curGame.teamOne)
            tvPlayerTwo.text = linkPlayerName(curGame.teamTwo)
            tvGameProgress.text = curGame.gameStatus
            tvRound.text = curGame.getRoundName(numRounds)
            btFinalizeButton.visibility = View.GONE
            btUnfinalizeButton.visibility = View.GONE
            etPlayerOneScore.visibility = View.INVISIBLE
            etPlayerTwoScore.visibility = View.INVISIBLE
            tvPlayerOneScore.visibility = View.VISIBLE
            tvPlayerTwoScore.visibility = View.VISIBLE

            if(privileges == "Admin") {
                if(curGame.gameStatus != "Final") {
                    btFinalizeButton.visibility = View.VISIBLE
                    btUnfinalizeButton.visibility = View.INVISIBLE
                    etPlayerOneScore.visibility = View.VISIBLE
                    etPlayerTwoScore.visibility = View.VISIBLE
                    tvPlayerTwoScore.visibility = View.INVISIBLE
                    tvPlayerOneScore.visibility = View.INVISIBLE

                    btFinalizeButton.setOnClickListener {
                        etPlayerOneScore.clearFocus()
                        etPlayerTwoScore.clearFocus()
                        if (curGame.gameStatus != "Final") {
                            val newGame = Game(curGame)
                            newGame.gameStatus = "Final"
                            tournamentGamesViewModel.updateOldGameToNewGameDatabase(
                                Pair(
                                    curGame.teamOne,
                                    curGame.teamTwo
                                ), newGame
                            )
                            notifyItemChanged(position)
                        }
                    }
                }
                else {
                    btFinalizeButton.visibility = View.INVISIBLE
                    btUnfinalizeButton.visibility = View.VISIBLE
                    etPlayerOneScore.visibility = View.INVISIBLE
                    etPlayerTwoScore.visibility = View.INVISIBLE
                    tvPlayerOneScore.visibility = View.VISIBLE
                    tvPlayerTwoScore.visibility = View.VISIBLE

                    btUnfinalizeButton.setOnClickListener {
                        if (curGame.gameStatus == "Final") {
                            val newGame = Game(curGame)
                            newGame.gameStatus = "In Progress"
                            tournamentGamesViewModel.updateOldGameToNewGameDatabase(
                                Pair(
                                    curGame.teamOne,
                                    curGame.teamTwo
                                ), newGame
                            )
                            notifyItemChanged(position)
                        }
                    }
                }

                etPlayerOneScore.setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        updatePlayerOneScore(curGame, holder)
                        true
                    } else {
                        false
                    }
                }
                etPlayerOneScore.setOnFocusChangeListener { view, hasFocus ->
                    if(!hasFocus && view.visibility == View.VISIBLE)
                    {
                        updatePlayerOneScore(curGame, holder)
                    }
                }
                etPlayerTwoScore.setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        updatePlayerTwoScore(curGame, holder)
                        true
                    } else {
                        false
                    }
                }
                etPlayerTwoScore.setOnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus && view.visibility == View.VISIBLE) {
                        updatePlayerTwoScore(curGame, holder)
                    }
                }

            }
            else{
                etPlayerOneScore.visibility = View.INVISIBLE
                etPlayerTwoScore.visibility = View.INVISIBLE
                btFinalizeButton.visibility = View.GONE
                btUnfinalizeButton.visibility = View.GONE
                tvPlayerOneScore.visibility = View.VISIBLE
                tvPlayerTwoScore.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return eachRoundGames[currentRound].size
    }

    private fun linkPlayerName(previousName: String): String {
        // Converting previous name to int
        val previousInt = previousName.toIntOrNull()
        return if (previousInt != null) {
            if (previousInt < players.size) {
                playerMap[players[previousInt]] ?: previousName
            } else {
                "TBD"
            }
        } else {
            playerMap[previousName] ?: previousName
        }
    }

    fun updatePlayerList(updatedPlayerList: List<String>) {
        players = updatedPlayerList
    }

    fun updatePlayerMap(updatedPlayerMap: Map<String, String>) {
        if(playerMap != updatedPlayerMap) {
            playerMap = updatedPlayerMap
            // TODO: Optimize these function calls
            notifyDataSetChanged()
        }
    }
}
