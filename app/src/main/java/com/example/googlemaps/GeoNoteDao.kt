package com.example.googlemaps

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface GeoNoteDao {
    @Query("SELECT * FROM geo_notes")
    fun getAllNotes(): Flow<List<GeoNote>>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: GeoNote): Long

    @Delete
    suspend fun deleteNote(note: GeoNote)

    @Query("DELETE FROM geo_notes")
    suspend fun deleteAllNotes()
}