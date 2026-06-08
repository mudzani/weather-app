package com.example.weatherarraysapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    // this is where I store all the days of the week
    private val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    // parallel arrays to hold min and max temps for each day
    private var minTemps = DoubleArray(7)
    private var maxTemps = DoubleArray(7)

    // conditions array to store the weather type for each day
    private var conditions = Array(7) { "" }

    // flag so I know if the user has calculated before trying to view details
    private var hasCalculated = false

    // I'm going to keep the EditTexts in arrays so I can loop through them
    private lateinit var etMinFields: Array<EditText>
    private lateinit var etMaxFields: Array<EditText>
    private lateinit var etCondFields: Array<EditText>

    private lateinit var tvAverageResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)

        Log.d(tag, "MainActivity loaded")

        // wiring up all the EditText fields by their IDs
        etMinFields = arrayOf(
            findViewById(R.id.etMinMon), findViewById(R.id.etMinTue),
            findViewById(R.id.etMinWed), findViewById(R.id.etMinThu),
            findViewById(R.id.etMinFri), findViewById(R.id.etMinSat),
            findViewById(R.id.etMinSun)
        )

        etMaxFields = arrayOf(
            findViewById(R.id.etMaxMon), findViewById(R.id.etMaxTue),
            findViewById(R.id.etMaxWed), findViewById(R.id.etMaxThu),
            findViewById(R.id.etMaxFri), findViewById(R.id.etMaxSat),
            findViewById(R.id.etMaxSun)
        )

        etCondFields = arrayOf(
            findViewById(R.id.etCondMon), findViewById(R.id.etCondTue),
            findViewById(R.id.etCondWed), findViewById(R.id.etCondThu),
            findViewById(R.id.etCondFri), findViewById(R.id.etCondSat),
            findViewById(R.id.etCondSun)
        )

        tvAverageResult = findViewById(R.id.tvAverageResult)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnViewDetails = findViewById<Button>(R.id.btnViewDetails)
        val btnExitMain = findViewById<Button>(R.id.btnExitMain)

        // calculate button reads the inputs, validates them, then shows the average
        btnCalculate.setOnClickListener {
            Log.d(tag, "Calculate button clicked")
            if (readAndValidateInputs()) {
                val avg = calculateAverageTemp(minTemps, maxTemps)
                tvAverageResult.text = "Average Temperature: %.1f°C".format(avg)
                hasCalculated = true
                Log.d(tag, "Average calculated: $avg")
            }
        }

        // clear button wipes everything so the user can start fresh
        btnClear.setOnClickListener {
            Log.d(tag, "Clear button clicked")
            clearAllData()
        }

        // view details sends the arrays to the detail screen
        btnViewDetails.setOnClickListener {
            Log.d(tag, "View Details button clicked")
            // I check if they've calculated first — no point showing empty data
            if (!hasCalculated) {
                Toast.makeText(this, "Please enter data and press Calculate first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, DetailActivity::class.java)
            // I'm packing all the arrays into the intent so DetailActivity can use them
            intent.putExtra("DAYS", days)
            intent.putExtra("MIN_TEMPS", minTemps)
            intent.putExtra("MAX_TEMPS", maxTemps)
            intent.putExtra("CONDITIONS", conditions)
            startActivity(intent)
        }

        btnExitMain.setOnClickListener {
            Log.d(tag, "Exit button clicked from MainActivity")
            finishAffinity()
        }
    }

    // this function reads all 7 days of input and checks if anything is wrong
    // if a field is empty or has bad data, it shows an error on that specific field
    private fun readAndValidateInputs(): Boolean {
        // I loop through all 7 days using the index
        for (i in days.indices) {
            val minText = etMinFields[i].text.toString().trim()
            val maxText = etMaxFields[i].text.toString().trim()
            val condText = etCondFields[i].text.toString().trim()

            // check if min temp field is empty
            if (minText.isEmpty()) {
                etMinFields[i].error = "Please enter min temp for ${days[i]}"
                etMinFields[i].requestFocus()
                return false
            }

            // check if max temp field is empty
            if (maxText.isEmpty()) {
                etMaxFields[i].error = "Please enter max temp for ${days[i]}"
                etMaxFields[i].requestFocus()
                return false
            }

            // check if condition field is empty
            if (condText.isEmpty()) {
                etCondFields[i].error = "Please enter weather condition for ${days[i]}"
                etCondFields[i].requestFocus()
                return false
            }

            // try to convert the text to a number — if it fails it means the input isn't a number
            try {
                minTemps[i] = minText.toDouble()
            } catch (e: NumberFormatException) {
                etMinFields[i].error = "Min temp for ${days[i]} must be a number"
                etMinFields[i].requestFocus()
                Log.d(tag, "NumberFormatException on min temp for ${days[i]}: $e")
                return false
            }

            try {
                maxTemps[i] = maxText.toDouble()
            } catch (e: NumberFormatException) {
                etMaxFields[i].error = "Max temp for ${days[i]} must be a number"
                etMaxFields[i].requestFocus()
                Log.d(tag, "NumberFormatException on max temp for ${days[i]}: $e")
                return false
            }

            // if we get here, everything is fine for this day
            conditions[i] = condText
        }

        Log.d(tag, "All inputs validated successfully")
        return true
    }

    // standalone function to calculate the average — I pass in both arrays
    // and average across all min and max values combined (14 values total)
    private fun calculateAverageTemp(minArr: DoubleArray, maxArr: DoubleArray): Double {
        var total = 0.0

        // I used a for loop here because we need to go through all 7 days
        for (i in minArr.indices) {
            total += minArr[i]
            total += maxArr[i]
        }

        // divide by 14 because we have 7 min + 7 max values
        return total / 14
    }

    // this resets everything back to default so the user can enter new data
    private fun clearAllData() {
        // reset the arrays to zero / empty string
        for (i in days.indices) {
            minTemps[i] = 0.0
            maxTemps[i] = 0.0
            conditions[i] = ""
        }

        // clear all the EditText fields on screen
        for (field in etMinFields) field.setText("")
        for (field in etMaxFields) field.setText("")
        for (field in etCondFields) field.setText("")

        // reset the result display
        tvAverageResult.text = "Average Temperature: --"

        // reset the flag so they have to calculate again before viewing details
        hasCalculated = false

        Toast.makeText(this, "All data cleared. You can re-enter your data now.", Toast.LENGTH_SHORT).show()
        Log.d(tag, "All data cleared")
    }
}