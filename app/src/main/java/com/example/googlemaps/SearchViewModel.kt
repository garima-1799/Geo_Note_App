package com.example.googlemaps

import androidx.lifecycle.AndroidViewModel

class  SearchViewModel(
    private val repo: GeoNoteRepository,
    private val app : GeoApplication): AndroidViewModel(app){

}