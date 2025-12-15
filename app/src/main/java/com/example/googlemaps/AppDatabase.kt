package com.example.googlemaps

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GeoNote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun geoNoteDao(): GeoNoteDao

    companion object{
        @Volatile private var INSTANCE : AppDatabase? = null
        fun getInstance(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,"geonotes.db").build()
                    .also {INSTANCE = it}
            }
    }
}