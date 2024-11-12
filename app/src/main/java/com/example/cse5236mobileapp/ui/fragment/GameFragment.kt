package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.ViewGameAdapter
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel

class GameFragment(private val game: Game, private val numRounds: Int) : Fragment(R.layout.fragment_game){


    // TODO: Is this even used at all?
    private lateinit var viewGameAdapter : ViewGameAdapter

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val rvGameView = view.findViewById<RecyclerView>(R.id.rvGameView)
//
//        //viewGameAdapter = ViewGameAdapter(mutableListOf())
//        rvGameView.adapter = viewGameAdapter
//        //rvGameView.layoutManager = LinearLayoutManager(this)
//
//    }
}