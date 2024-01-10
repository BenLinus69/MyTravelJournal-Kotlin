package com.example.finalproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

class TravelMemoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val placeNameTextView: TextView = itemView.findViewById(R.id.textViewPlaceName)
    val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
    val favoriteToggleButton: ToggleButton = itemView.findViewById(R.id.toggleButtonFavorite)
    val detailsButton: Button=itemView.findViewById(R.id.detailsButton)
}

class TravelMemoryAdapter(
    private val travelMemories: List<TravelMemory>,
    private val onFavoriteClick: (TravelMemory) -> Unit,
    private val onDetailsClick: (TravelMemory) -> Unit
) : RecyclerView.Adapter<TravelMemoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelMemoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.travel_memory_item, parent, false)
        return TravelMemoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelMemoryViewHolder, position: Int) {
        val currentMemory = travelMemories[position]

        // bind memory data to views
        holder.placeNameTextView.text = currentMemory.placeName
        holder.dateTextView.text = currentMemory.dateOfTravel
        holder.favoriteToggleButton.isChecked = currentMemory.isFavorite


        // favorite click listener
        holder.favoriteToggleButton.setOnCheckedChangeListener { _, isChecked ->
            currentMemory.isFavorite = isChecked
            onFavoriteClick.invoke(currentMemory)
        }
        //details click listener
        holder.detailsButton.setOnClickListener {
            onDetailsClick.invoke(currentMemory)
        }
    }

    override fun getItemCount(): Int {
        return travelMemories.size
    }
}
