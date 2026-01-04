package com.example.googlemaps

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.googlemaps.data.local.AppDatabase
import com.example.googlemaps.data.repository.GeoNoteRepository
import com.example.googlemaps.ui.MapFragment
import com.example.googlemaps.viewmodel.MapViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var viewModel: MapViewModel
    public lateinit var viewModelFactory: MapViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = MainPagerAdapter(this)

        viewPager.isUserInputEnabled = false

        val db = AppDatabase.getInstance(this)
        val repository = GeoNoteRepository(db.geoNoteDao())

        viewModelFactory = MapViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MapViewModel::class.java)

        if (savedInstanceState == null) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Map" else "Places"
        }.attach()

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MapFragment())
//                .commit()
        }
    }
    fun switchToMapTab() {
        viewPager.currentItem = 0
    }
}