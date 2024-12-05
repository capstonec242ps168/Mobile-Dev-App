package com.valdo.refind.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.valdo.refind.R
import com.valdo.refind.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        // Check if user is signed in
        if (currentUser != null) {
            val userName = currentUser.displayName
            val userEmail = currentUser.email
            val userPhoto = currentUser.photoUrl

            // Display the user details
            binding.userName.text = userName ?: "No Name"
            binding.userEmail.text = userEmail ?: "No Email"

            // Load user photo using Glide
            Glide.with(this)
                .load(userPhoto)
                .placeholder(R.drawable.ic_place_holder) // Optional: Add a placeholder image
                .apply(RequestOptions.circleCropTransform()) // Make the image circular
                .into(binding.profileImage)
        } else {
            Log.w("SettingFragment", "No user is signed in")
        }



        // Hide navigation items
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        // About button
        binding.aboutApp.setOnClickListener {
            Log.d("SettingFragment", "About button clicked")
            val fragment = AboutFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Logout button
        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        auth.signOut() // Sign out from Firebase
        val intent = Intent(requireActivity(), LandingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_settings)?.isVisible = false // Hides settings icon
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Show navigation items again
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}
