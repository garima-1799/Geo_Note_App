package com.example.googlemaps

import kotlinx.coroutines.flow.Flow;

class GeoNoteRepository (private val dao: GeoNoteDao){
    fun allNotes():Flow<List<GeoNote>> =  dao.getAllNotes()

    suspend fun insertNote(note: GeoNote) :Long = dao.insertNote(note)

//    suspend fun deleteNote(note: GeoNote) = dao.deleteNote(note)
}
