package com.example.googlemaps

import android.app.Application
import com.example.googlemaps.data.local.AppDatabase
import com.google.android.libraries.places.api.Places

class GeoApplication: Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
       /* database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "geo_notes.db"
        ).build()*/

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_api_key))
        }
    }
}
