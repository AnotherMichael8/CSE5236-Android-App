package com.example.cse5236mobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.databinding.FragmentIntroPageBinding



class IntroPageFragment : Fragment() {

    private lateinit var binding: FragmentIntroPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentIntroPageBinding.inflate(inflater, container, false)
        return binding.root
    }

}