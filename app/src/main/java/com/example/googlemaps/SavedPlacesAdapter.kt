package com.example.googlemaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaps.data.local.GeoNote

class SavedPlacesAdapter (
    private val onItemClick: (GeoNote) -> Unit
) : ListAdapter<GeoNote, SavedPlacesAdapter.PlaceViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvTitle)
        private val description = itemView.findViewById<TextView>(R.id.tvDescription)

        fun bind(note: GeoNote) {
            title.text = note.title
            description.text = note.description

            itemView.setOnClickListener {
                onItemClick(note)
            }
        }
    }

    companion object {
        private val Diff = object : DiffUtil.ItemCallback<GeoNote>() {
            override fun areItemsTheSame(old: GeoNote, new: GeoNote) =
                old.id == new.id

            override fun areContentsTheSame(old: GeoNote, new: GeoNote) =
                old == new
        }
    }
}