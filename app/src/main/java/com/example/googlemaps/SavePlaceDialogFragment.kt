package com.example.googlemaps

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.text.isNotBlank

class SavePlaceDialogFragment(
    private val latLng: LatLng,
    private val onSave: (String, String?) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_save_place, null)

        val nameEt = view.findViewById<EditText>(R.id.name_TV)
        val descEt = view.findViewById<EditText>(R.id.des_TV)

        return AlertDialog.Builder(requireContext())
            .setTitle("Save Place")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val name = nameEt.text.toString()
                val desc = descEt.text.toString()

                if (name.isNotBlank()) {
                    onSave(name, desc)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
