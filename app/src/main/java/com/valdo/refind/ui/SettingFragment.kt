package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.replace
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

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        // Find the 'about_app' button in the layout
        val aboutButton = view.findViewById<Button>(R.id.about_app)
        val editProfile = view.findViewById<Button>(R.id.edit_profile)

        // Hide profile and setting button
        setHasOptionsMenu(true)

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

        editProfile.setOnClickListener {
            val fragment = EditProfileFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.action_settings)?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}