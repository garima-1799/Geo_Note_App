📍 GeoNote – Save Places on Map

GeoNote is an Android app that lets users search places on Google Maps, save them with notes, and navigate back to them later using a synced map and list view.

✨ Features

 🗺️ Google Maps with current location

🔍 Place search using Google Places API

📌 Save places with title and description

🗂️ View all saved places in a list

🔄 Map ↔ Saved Places synchronization

✏️ Edit & delete saved places via marker info window

🧭 TabLayout navigation (Map / Saved Places)

🚫 Swipe disabled to avoid accidental navigation

📸 Screenshots

<img width="540" height="1170" alt="Screenshot_20260104_225608"
src="https://github.com/user-attachments/assets/8c16ec77-82c9-4982-9fdc-c74ccc45aaf7" />  <img width="540" height="1170" alt="Screenshot_20260104_225710" src="https://github.com/user-attachments/assets/3563132d-e14e-4bd1-ac85-943f822a90b4" />  <img width="540" height="1170" alt="Screenshot_20260104_225817" src="https://github.com/user-attachments/assets/f9714165-c71b-4b0c-b08b-a16c37ae2574" />  <img width="540" height="1170" alt="Screenshot_20260104_225832" src="https://github.com/user-attachments/assets/3934fbe4-0ead-4a8a-bf7f-a8cb3d47698d" />

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

   Portfolio project focused on clean architecture and real-world Android 
   patterns.



⭐ Feel free to star the repo and share feedback!
