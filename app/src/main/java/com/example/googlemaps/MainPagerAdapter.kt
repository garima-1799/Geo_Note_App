package com.example.googlemaps

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.googlemaps.ui.MapFragment
import com.example.googlemaps.ui.SavedPlacesFragment

class MainPagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MapFragment()
            else -> SavedPlacesFragment()
        }
    }
}
