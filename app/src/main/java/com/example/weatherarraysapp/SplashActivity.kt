package com.example.weatherarraysapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fixed: matching the renamed layout file splash_activity.xml
        setContentView(R.layout.splash_activity)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnExit = findViewById<Button>(R.id.btnExit)

        btnStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }
}