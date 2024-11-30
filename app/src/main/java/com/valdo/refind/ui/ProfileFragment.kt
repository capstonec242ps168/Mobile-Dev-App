package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.valdo.refind.R

class ProfileFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide Bottom Navigation Views
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        // Set Click Listeners for Icons
        val editUsernameIcon = view.findViewById<ImageView>(R.id.edit_username_icon)
        val editEmailIcon = view.findViewById<ImageView>(R.id.edit_email_icon)

        editUsernameIcon.setOnClickListener {
            navigateToEditProfile()
        }

        editEmailIcon.setOnClickListener {
            navigateToEditProfile()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun navigateToEditProfile() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, EditProfileFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.action_settings)?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        // Restore Bottom Navigation Views
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}
