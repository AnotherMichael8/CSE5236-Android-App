package com.example.cse5236mobileapp.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import android.view.View

class PlayerNameAdapter(private val playerList : MutableList<String>) :
    RecyclerView.Adapter<PlayerNameAdapter.PlayerViewHolder>() {

        class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val playerName : TextView = view.findViewById(R.id.txtPlayerNameRecycle)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_name, parent, false))
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val current = playerList[position]
        holder.playerName.text = current
    }

    fun updateNames(updatedNames: List<String>) {
        playerList.removeAll(playerList)
        playerList.addAll(updatedNames)
        //TODO: optimize this function call to notifyDataSetChanged()
        notifyDataSetChanged()
        Log.d("PlayerNameAdapter", "Updated")
    }

}