package com.example.googlemaps

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    repository: GeoNoteRepository
) : ViewModel() {

  /*val savedPlaces: StateFlow<List<GeoNote>> =
        repository.allNotes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )*/

}
