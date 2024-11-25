package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.valdo.refind.R

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the 'about_app' button in the layout
        val aboutButton = view.findViewById<Button>(R.id.about_app)

        // Set an onClickListener to navigate to AboutFragment
        aboutButton.setOnClickListener {
            Log.d("SettingFragment", "About button clicked")

            // Navigate to AboutFragment using FragmentTransaction
            val fragment = AboutFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your container ID
                .addToBackStack(null) // Allow navigation back to SettingFragment
                .commit()
        }
    }
}