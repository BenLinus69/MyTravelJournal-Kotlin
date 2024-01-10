package com.example.finalproject

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import java.util.Calendar

class AddMemoryActivity : AppCompatActivity() {
    private lateinit var editTextPlace: EditText
    private lateinit var editTravelType: Spinner
    private lateinit var editTravelMood: SeekBar
    private lateinit var buttonAddMemory: Button
    private lateinit var datePickerButton: Button
    private lateinit var dbHelper: TravelMemoryDbHelper
    private val travelTypes = arrayOf("Leisure", "Business", "Family", "Adventure")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memory)
        dbHelper = TravelMemoryDbHelper(this)

        // Initialize views
        editTextPlace = findViewById(R.id.editTextPlace)
        editTravelType= findViewById(R.id.editTravelType)
        buttonAddMemory = findViewById(R.id.buttonAddMemory)
        datePickerButton = findViewById(R.id.datePickerButton)
        editTravelMood = findViewById(R.id.editTravelMood)

        //SLIDER MENU
        var moodLevel=""
        editTravelMood.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                 moodLevel = when {
                    progress < 33 -> "Sad"
                    progress in 33..66 -> "Neutral"
                    else -> "Happy"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        //DROPDOWN MENU
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, travelTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTravelType.adapter=adapter

        //DATE ADD
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        var selectedDate=""
        datePickerButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        buttonAddMemory.setOnClickListener {
            val placeName = editTextPlace.text.toString()
            val travelType =editTravelType.selectedItem.toString()

            if (placeName.isNotEmpty() && selectedDate.isNotEmpty() && travelType.isNotEmpty()) {
                val newMemory = TravelMemory(placeName = placeName, dateOfTravel = selectedDate , travelType = travelType , travelMood = moodLevel)//build a Memory

                val insertedId = dbHelper.insertTravelMemory(newMemory)//insert memory in DB

                if (insertedId != -1L) {
                    finish()
                } else {
                    Log.d("eroareInsert", "eroareInsert")
                }
            } else {
                Log.d("eroareinput", "eroareinput")
            }
        }
    }
}