package com.example.finalproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TravelMemoryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "TravelMemoryDB"

        private const val TABLE_TRAVEL_MEMORY = "travel_memories"
        private const val KEY_ID = "id"
        private const val KEY_PLACE_NAME = "place_name"
        private const val KEY_DATE_OF_TRAVEL = "date_of_travel"
        private const val KEY_TRAVEL_TYPE = "travel_type"
        private const val KEY_TRAVEL_MOOD = "travel_mood"
        private const val KEY_IS_FAVORITE = "is_favorite"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_TRAVEL_MEMORY("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_PLACE_NAME TEXT,"
                + "$KEY_DATE_OF_TRAVEL TEXT,"
                + "$KEY_TRAVEL_TYPE TEXT,"
                + "$KEY_TRAVEL_MOOD TEXT,"
                + "$KEY_IS_FAVORITE INTEGER)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRAVEL_MEMORY")
        onCreate(db)
    }
    fun deleteAllTravelMemories() {
        val db = this.writableDatabase
        db.delete(TABLE_TRAVEL_MEMORY, null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllTravelMemories(): List<TravelMemory> {
        val memoriesList = mutableListOf<TravelMemory>()
        val selectQuery = "SELECT * FROM $TABLE_TRAVEL_MEMORY"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {//extract data from the cells
                val id = cursor.getLong(cursor.getColumnIndex(KEY_ID))
                val placeName = cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME))
                val dateOfTravel = cursor.getString(cursor.getColumnIndex(KEY_DATE_OF_TRAVEL))
                val travelType = cursor.getString(cursor.getColumnIndex(KEY_TRAVEL_TYPE))
                val travelMood = cursor.getString(cursor.getColumnIndex(KEY_TRAVEL_MOOD))
                val isFavorite = cursor.getInt(cursor.getColumnIndex(KEY_IS_FAVORITE)) == 1

                val travelMemory = TravelMemory(id, placeName, dateOfTravel,travelType, travelMood,isFavorite)
                memoriesList.add(travelMemory)//add the memory to the List
            } while (cursor.moveToNext())
        }

        cursor.close()
        return memoriesList
    }
    @SuppressLint("Range")
    fun getFavoriteTravelMemories(): List<TravelMemory> {
        val memoriesList = mutableListOf<TravelMemory>()
        val selectQuery = "SELECT * FROM $TABLE_TRAVEL_MEMORY WHERE $KEY_IS_FAVORITE = 1"
        //only select favorited memories
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(KEY_ID))
                val placeName = cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME))
                val dateOfTravel = cursor.getString(cursor.getColumnIndex(KEY_DATE_OF_TRAVEL))
                val travelType = cursor.getString(cursor.getColumnIndex(KEY_TRAVEL_TYPE))
                val travelMood = cursor.getString(cursor.getColumnIndex(KEY_TRAVEL_MOOD))
                val isFavorite = cursor.getInt(cursor.getColumnIndex(KEY_IS_FAVORITE)) == 1

                val travelMemory = TravelMemory(id, placeName, dateOfTravel, travelType,travelMood,isFavorite)
                memoriesList.add(travelMemory)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return memoriesList
    }


    fun insertTravelMemory(memory: TravelMemory): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PLACE_NAME, memory.placeName)
            put(KEY_DATE_OF_TRAVEL, memory.dateOfTravel)
            put(KEY_TRAVEL_TYPE, memory.travelType)
            put(KEY_TRAVEL_MOOD, memory.travelMood)
            put(KEY_IS_FAVORITE, if (memory.isFavorite) 1 else 0)
        }

        return db.insert(TABLE_TRAVEL_MEMORY, null, values)
    }

    fun updateTravelMemory(memory: TravelMemory): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PLACE_NAME, memory.placeName)
            put(KEY_DATE_OF_TRAVEL, memory.dateOfTravel)
            put(KEY_TRAVEL_TYPE, memory.travelType)
            put(KEY_TRAVEL_MOOD, memory.travelMood)
            put(KEY_IS_FAVORITE, if (memory.isFavorite) 1 else 0)
        }

        return db.update(
            TABLE_TRAVEL_MEMORY,
            values,
            "$KEY_ID = ?",
            arrayOf(memory.id.toString())
        )
    }
}
