package com.example.googlemaps

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GeoNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: GeoNote): Long

    @Query("SELECT * FROM geo_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<GeoNote>>
}
