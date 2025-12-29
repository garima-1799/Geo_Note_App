package com.example.googlemaps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemaps.data.local.GeoNote
import com.example.googlemaps.data.repository.GeoNoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: GeoNoteRepository
) : ViewModel() {
    val savedPlaces: StateFlow<List<GeoNote>> =
        repo.allNotes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    fun savePlace( title: String,
                   description: String,
                   lat: Double,
                   lng: Double) {

        viewModelScope.launch {
            repo.insertNote(
                GeoNote(
                    title = title,
                    description = description,
                    latitude = lat,
                    longitude = lng,
                    createdAt = System.currentTimeMillis()
                )
            )
        }

    }

    fun updatePlace(place: GeoNote) {
        viewModelScope.launch {
            repo.updateNote(place)
        }
    }

    fun deletePlace(place: GeoNote) {
        viewModelScope.launch {
            repo.deleteNote(place)
        }
    }

}
