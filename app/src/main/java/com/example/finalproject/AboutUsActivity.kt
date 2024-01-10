package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        val buttonBackToMain = findViewById<Button>(R.id.buttonBack)
        buttonBackToMain.setOnClickListener {
            // Navigate back to MainActivity
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish() // Finish the AboutUsActivity
        }
    }
}