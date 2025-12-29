package com.example.googlemaps.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geo_notes")
data class GeoNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: Long = System.currentTimeMillis()
)
