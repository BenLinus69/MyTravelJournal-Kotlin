package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val memoryDetails = intent.getSerializableExtra("MEMORY_DETAILS") as TravelMemory

        val placeNameTextView: TextView = findViewById(R.id.placeNameTextView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val travelTypeTextView: TextView = findViewById(R.id.travelTypeTextView)
        val travelMoodTextView: TextView=findViewById(R.id.travelMoodTextView)
        //assign memory details to views
        placeNameTextView.text = memoryDetails.placeName
        dateTextView.text = memoryDetails.dateOfTravel
        travelTypeTextView.text = memoryDetails.travelType
        travelMoodTextView.text=memoryDetails.travelMood

        val buttonBackToMain = findViewById<Button>(R.id.buttonBack)
        buttonBackToMain.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}