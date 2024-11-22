package com.example.cse5236mobileapp.ui.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import com.example.cse5236mobileapp.R
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Main Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button_continue = findViewById<Button>(R.id.btnContinue)

        button_continue.setOnClickListener{
            Firebase.analytics.logEvent("ContinueFromHome", null)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.i(TAG, "Switching to Log In Screen")
        }
    }
}