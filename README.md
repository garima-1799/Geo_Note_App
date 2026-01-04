📍 GeoNote – Save Places on Map

GeoNote is an Android app that lets users search places on Google Maps, save them with notes, and navigate back to them later using a synced map and list view.

✨ Features

🗺️ Google Maps with current location

🔍 Place search using Google Places API

📌 Save places with title & description

🗂️ View saved places in a list

🔄 Map ↔ List synchronization

✏️ Edit & delete saved places

🏗️ Architecture


Built using MVVM architecture with a shared ViewModel:
MainActivity

 ├── MapFragment
 
 ├── SavedPlacesFragment
 
 └── MapViewModel
       ↓
   Repository
       ↓
     Room DB


Single source of truth

No fragment-to-fragment communication

State handled using StateFlow


🛠️ Tech Stack


Kotlin

MVVM

Google Maps & Places API

Room Database

Coroutines & StateFlow

RecyclerView + ListAdapter + DiffUtil


🚀 How It Works


Saving a place updates the database

List updates automatically via StateFlow

Clicking a saved place switches to the Map tab

The camera moves to the selected location and highlights the marker


📌 Author

Garima Biswakarma

Android Developer

Portfolio project focused on clean architecture and real-world Android patterns.



⭐ Feel free to star the repo and share feedback!
