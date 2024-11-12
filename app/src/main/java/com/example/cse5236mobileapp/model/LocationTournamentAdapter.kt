package com.example.cse5236mobileapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.ViewGameAdapter.GameViewHolder

class LocationTournamentAdapter (
) : RecyclerView.Adapter<LocationTournamentAdapter.PublicTournamentViewHolder>(){

    private var publicTournaments : MutableList<TournamentIdentifier> = mutableListOf()

    class PublicTournamentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress : TextView = itemView.findViewById(R.id.tvAddress)
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)
        val tvTournamentName: TextView = itemView.findViewById(R.id.tvTournmentNameView)
        var tvDist: TextView = itemView.findViewById(R.id.tvDist)
        val btJoin : Button = itemView.findViewById(R.id.btJoin)
    }
    fun updatePublicTournaments(newTournament: List<TournamentIdentifier>)
    {
        publicTournaments.clear()
        for(tournament in newTournament)
        {
            if(!tournament.tournament.isPrivate)
            {
                publicTournaments.add(tournament);
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicTournamentViewHolder {
        return PublicTournamentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.single_tournament_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return publicTournaments.size
    }

    override fun onBindViewHolder(holder: PublicTournamentViewHolder, position: Int) {
        val curTournament = publicTournaments[position]
        holder.apply {
            tvAddress.text = curTournament.tournament.address
            tvTime.text = curTournament.tournament.time
            tvDate.text = curTournament.tournament.date
            tvTournamentName.text = curTournament.tournament.tournamentName
            val dist = "0.62mi"
            tvDist.text = dist

            btJoin.setOnClickListener {

            }
        }
    }
}