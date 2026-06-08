package com.example.weatherarraysapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private val tag = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailactivity)

        Log.d(tag, "DetailActivity loaded")

        // grab the container where I'll add rows for each day
        val container = findViewById<LinearLayout>(R.id.containerDays)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // receive the arrays from MainActivity — use fallback if something comes through as null
        val days = intent.getStringArrayExtra("DAYS") ?: arrayOf()
        val minTemps = intent.getDoubleArrayExtra("MIN_TEMPS") ?: DoubleArray(7)
        val maxTemps = intent.getDoubleArrayExtra("MAX_TEMPS") ?: DoubleArray(7)
        val conditions = intent.getStringArrayExtra("CONDITIONS") ?: arrayOf()

        // I loop through all the days and build a card for each one
        for (i in days.indices) {
            Log.d(tag, "Building row for ${days[i]}")

            // outer card layout
            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(16, 16, 16, 16)
            card.setBackgroundColor(0xFF1E3A5F.toInt())

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 12)
            card.layoutParams = params

            // day name label
            val tvDay = TextView(this)
            tvDay.text = days[i]
            tvDay.textSize = 18f
            tvDay.setTextColor(0xFF90CAF9.toInt())
            tvDay.typeface = android.graphics.Typeface.DEFAULT_BOLD
            card.addView(tvDay)

            // min temp
            val tvMin = TextView(this)
            tvMin.text = "Min Temp: ${minTemps[i]}°C"
            tvMin.textSize = 14f
            tvMin.setTextColor(0xFFFFFFFF.toInt())
            card.addView(tvMin)

            // max temp
            val tvMax = TextView(this)
            tvMax.text = "Max Temp: ${maxTemps[i]}°C"
            tvMax.textSize = 14f
            tvMax.setTextColor(0xFFFFFFFF.toInt())
            card.addView(tvMax)

            // condition
            val tvCond = TextView(this)
            // index i lets me access the same position across all arrays
            tvCond.text = "Condition: ${if (i < conditions.size) conditions[i] else "N/A"}"
            tvCond.textSize = 14f
            tvCond.setTextColor(0xFFFFD54F.toInt())
            card.addView(tvCond)

            // add this card to the scrollable container
            container.addView(card)
        }

        // finish() goes back without needing a new Intent
        btnBack.setOnClickListener {
            Log.d(tag, "Back button clicked")
            finish()
        }
    }
}