package com.example.googlemaps.data.repository

import com.example.googlemaps.data.local.GeoNote
import com.example.googlemaps.data.local.GeoNoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GeoNoteRepository(private val dao: GeoNoteDao){
    fun allNotes():Flow<List<GeoNote>> =  dao.getAllNotes()

    suspend fun insertNote(note: GeoNote) :Long = dao.insertNote(note)

    suspend fun deleteNote(note: GeoNote) = dao.deleteNote(note)

    suspend fun updateNote(note: GeoNote) = dao.updateNote(note)
}
