package com.example.googlemaps.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GeoNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: GeoNote): Long

    @Query("SELECT * FROM geo_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<GeoNote>>

    @Update
    suspend fun updateNote(note: GeoNote)

    @Delete
    suspend fun deleteNote(note: GeoNote)

}
