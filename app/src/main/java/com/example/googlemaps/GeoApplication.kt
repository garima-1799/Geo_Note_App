package com.example.googlemaps

import android.app.Application
import androidx.room.Room

class GeoApplication: Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,
            "geo_notes.db")
            .build()
        com.google.android.libraries.places.api.Places.initialize(applicationContext, "AIzaSyCvRqMCadioMVFGF1u89l2wTA6gS6hOjNw")

    }
}