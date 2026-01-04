package com.example.googlemaps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaps.MainActivity
import com.example.googlemaps.R
import com.example.googlemaps.SavedPlacesAdapter
import com.example.googlemaps.viewmodel.MapViewModel
import kotlinx.coroutines.launch

class SavedPlacesFragment : Fragment(R.layout.fragment_saved_places) {
    private val viewModel: MapViewModel by activityViewModels {
        (requireActivity() as MainActivity).viewModelFactory
    }
    private lateinit var adapter: SavedPlacesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setPadding(0, 0, 0, 0)
        recyclerView.clipToPadding = false
        recyclerView.clipChildren = false
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        adapter = SavedPlacesAdapter { geoNote ->
            viewModel.selectPlace(geoNote)
            (requireActivity() as MainActivity).switchToMapTab()
        }

        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedPlaces.collect { places ->
                    adapter.submitList(places)
                }
            }
        }

    }
}