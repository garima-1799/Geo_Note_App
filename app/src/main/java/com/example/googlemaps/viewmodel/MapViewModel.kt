package com.example.googlemaps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemaps.data.local.GeoNote
import com.example.googlemaps.data.repository.GeoNoteRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapViewModel (
    private val repo: GeoNoteRepository
) : ViewModel() {
    val selectedPlace = MutableSharedFlow<GeoNote>()
    val place = selectedPlace.asSharedFlow()
//    val savedPlace = repo.allNotes()

    fun selectPlace(note: GeoNote) {
        viewModelScope.launch {
            selectedPlace.emit(note)
        }
    }
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
//    suspend fun clearSelection() {
//        selectedPlace.emit(null)
//    }
}
