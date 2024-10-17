package com.example.cse5236mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log;
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeScreenActivity : AppCompatActivity() {
    lateinit var user: Account
    lateinit var currentUser: FirebaseUser

    companion object {
        private const val TAG = "Home Screen Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Getting username from intent

        user = intent.getSerializableExtra("user") as Account
        val username = user.username


        findViewById<TextView>(R.id.txtHomeScreenWelcome).text = "Welcome: $username"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button_logout = findViewById<Button>(R.id.LogoutButton)
        val settings_button = findViewById<ImageButton>(R.id.settingsButton)

        button_logout.setOnClickListener{
            Firebase.auth.signOut()
            finish()
            Log.i(TAG, "Logging Out")
        }

        settings_button.setOnClickListener{
            val settingsFrag = AccountSettingsFragment()
            supportFragmentManager.beginTransaction().add(R.id.frgSettingsContainer, settingsFrag).commit()
            Log.i(TAG, "Going to Settings Fragment")
        }
    }
}