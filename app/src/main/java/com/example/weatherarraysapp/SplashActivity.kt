package com.example.weatherarraysapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SplashActivity : AppCompatActivity() {

    // tag I use for logging so I can filter in Logcat
    private val tag = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        Log.d(tag, "Splash screen loaded")

        // finding my buttons in the layout
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnExit = findViewById<Button>(R.id.btnExit)

        // when start is clicked, open the main screen
        btnStart.setOnClickListener {
            Log.d(tag, "Start button clicked, going to MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // exit closes everything, not just this screen
        btnExit.setOnClickListener {
            Log.d(tag, "Exit button clicked, closing app")
            finishAffinity()
        }
    }
}