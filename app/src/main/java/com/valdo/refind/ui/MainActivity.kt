package com.valdo.refind.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import android.Manifest
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.valdo.refind.R
import com.valdo.refind.databinding.ActivityMainBinding
import com.valdo.refind.ui.MainActivity.Companion.CameraX_Permissions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions if not granted
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CameraX_Permissions, 0
            )
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))

        // Bottom navigation listener
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
            openFragment(ScanFragment())
        }

        // Listen for back stack changes to update toolbar title
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val title = when (fragment) {
                is ProfileFragment -> "Profile"
                is BookmarkFragment -> "Bookmarks"
                is SettingFragment -> "Settings"
                is EditProfileFragment -> "Edit Profile"
                is AboutFragment -> "About us"
                else -> getString(R.string.app_name)
            }
            supportActionBar?.title = title
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                openFragment(ProfileFragment())
                return true
            }
            R.id.action_settings -> {
                openFragment(SettingFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // Handle drawer if open, otherwise handle fragment back navigation
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Check if there's more than one fragment in the back stack
            val backStackCount = supportFragmentManager.backStackEntryCount

            if (backStackCount > 1) {
                // If there are fragments in the back stack, pop the current fragment and show the previous one
                super.onBackPressed()
            } else {
                // If there is only one fragment (the root fragment), exit the app
                // Alternatively, you could handle the behavior differently, e.g., prompt user before exiting
                finish()
            }
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        updateToolbar(currentFragment ?: HomeFragment())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Handle back navigation
        return true
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTag = fragment::class.java.simpleName

        // Check if the fragment is already in the container
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null && currentFragment::class.java.simpleName == fragmentTag) {
            return // Prevent reloading the same fragment
        }

        // Replace the fragment and add to back stack
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(fragmentTag) // Use the class name as the tag
            .commit()

        updateToolbar(fragment)
    }

    private fun updateToolbar(fragment: Fragment) {
        // Check the fragment type and adjust the toolbar accordingly
        if (fragment is HomeFragment) {
            // Hide the home button (back button) for HomeFragment
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            // Show the home button (back button) for other fragments
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CameraX_Permissions.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with camera usage
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val CameraX_Permissions = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
