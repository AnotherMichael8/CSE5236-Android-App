package com.example.cse5236mobileapp.model

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.ViewGameAdapter.GameViewHolder
import com.example.cse5236mobileapp.model.viewmodel.GeocoderViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationTournamentAdapter (
    private val listener: OnTournamentClickListener
) : RecyclerView.Adapter<LocationTournamentAdapter.PublicTournamentViewHolder>(){

    private var publicTournaments : MutableList<TournamentIdentifier> = mutableListOf()
    private var userLocation: Location? = null

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
            publicTournaments.add(tournament);
        }
        notifyDataSetChanged()
    }
    fun updateUserCurrentLocation(location: Location)
    {
        userLocation = location
        notifyDataSetChanged()
    }
    private fun getDistance(uLocation: LatLng, tourneyLocation: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(uLocation, tourneyLocation) * .000621371
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
            if(curTournament.tournament.latLng != null && userLocation != null) {
                val temp = LatLng(userLocation!!.latitude, userLocation!!.longitude)
                val dist = getDistance(
                    temp,
                    curTournament.tournament.latLng!!
                )
                tvDist.text = String.format("%.2fmi", dist)
            }
            else
            {
                tvDist.text = "N/A"
            }
            btJoin.setOnClickListener {

            }
            itemView.setOnClickListener(){
                listener.onTournamentClick(curTournament.tournament)
            }
        }
    }
}