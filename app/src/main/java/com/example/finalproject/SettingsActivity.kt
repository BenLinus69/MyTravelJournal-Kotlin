package com.example.finalproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val radioGroupTheme: RadioGroup = findViewById(R.id.radioGroupTheme)
        val radioButtonLight: RadioButton = findViewById(R.id.radioButtonLight)
        val radioButtonDark: RadioButton = findViewById(R.id.radioButtonDark)

        val selectedTheme = sharedPreferences.getInt("SelectedTheme", -1)//gets the current theme
        if (selectedTheme != -1) {
            if (selectedTheme == R.id.radioButtonLight) {//if light is checked, set light theme
                radioButtonLight.isChecked = true
                setTheme(R.style.Light_Theme)
            } else {//if dark is checked, set dark theme
                radioButtonDark.isChecked = true
                setTheme(R.style.Dark_Theme)
                Log.d("DARK", "Good");
            }
        }
        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->

            editor.putInt("SelectedTheme", checkedId)
            editor.apply()
            recreate()//recreates the activity to call onCreate again
        }

        //Set Language ?

        //Return to MainActivity
        val buttonBackToMain = findViewById<Button>(R.id.buttonBack)
        buttonBackToMain.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}