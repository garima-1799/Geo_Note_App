package com.example.googlemaps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
