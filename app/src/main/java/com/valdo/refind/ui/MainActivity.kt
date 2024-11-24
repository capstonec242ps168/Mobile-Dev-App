package com.valdo.refind.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.valdo.refind.R
import com.valdo.refind.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = supportFragmentManager

        // Bottom navigation item selection listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_bookmark -> openFragment(BookmarkFragment())
            }
            true
        }

        // Open HomeFragment by default
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }

        // Floating Action Button (FAB) click listener
        binding.fab.setOnClickListener {
            Toast.makeText(this, "scan", Toast.LENGTH_SHORT).show()
            // Implement scanning functionality here
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        // Only replace fragment if it's not already in the container
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null || currentFragment::class != fragment::class) {
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null) // Add to back stack for proper navigation
            fragmentTransaction.commit()
        }
    }
}
