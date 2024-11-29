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
/*      permission
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CameraX_Permissions, 0
            )
        } */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
    }

    private fun hasRequiredPermissions(): Boolean {
        return CameraX_Permissions.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CameraX_Permissions = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
