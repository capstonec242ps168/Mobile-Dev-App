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
import android.content.Context
import androidx.core.app.ActivityCompat
import com.valdo.refind.R
import com.valdo.refind.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CameraX_Permissions, 0
            )
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    openFragment(HomeFragment())
                }
                R.id.bottom_bookmark -> {
                    openFragment(BookmarkFragment())
                }
            }
            true
        }

        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }

        binding.fab.setOnClickListener {
            openFragment(ScanFragment())
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val title = when (fragment) {
                is BookmarkFragment -> "Tersimpan"
                is SettingFragment -> "Pengaturan"
                is AboutFragment -> "Tentang"
                is CraftFragment -> "Daftar Kerajinan"
                is ScanFragment -> "Pemindai"
                is ResultFragment -> "Hasil"
                is DetailNewsFragment -> "Berita"
                is DetailCraftFragment -> "Kerajinan"
                is NoCraftFragment -> {
                    if (fragment.arguments?.getBoolean("isCraft") == true) {
                        "Kerajinan"
                    } else {
                        "Hasil"
                    }
                }
                else -> getString(R.string.app_name)
            }
            supportActionBar?.title = title
        }
    }
    
    private fun updateHomeIcon(fragment: Fragment) {
        val homeItem = binding.bottomNavigation.menu.findItem(R.id.bottom_home)
        if (fragment is HomeFragment) {
            homeItem.setIcon(R.drawable.baseline_home_24)
        } else {
            homeItem.setIcon(R.drawable.outline_home_24)
        }
    }

    private fun updateBookmarkIcon(fragment: Fragment) {
        val bookmarkItem = binding.bottomNavigation.menu.findItem(R.id.bottom_bookmark)
        if (fragment is BookmarkFragment) {
            bookmarkItem.setIcon(R.drawable.baseline_bookmark_24)
        } else {
            bookmarkItem.setIcon(R.drawable.baseline_bookmark_border_24)
        }
    }

    fun enableFAB(enable: Boolean) {
        binding.fab.isEnabled = enable
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
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
            val backStackCount = supportFragmentManager.backStackEntryCount

            if (backStackCount > 1) {
                super.onBackPressed()
            } else {
                finish()
            }
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        updateToolbar(currentFragment ?: HomeFragment())
        updateHomeIcon(currentFragment ?: HomeFragment())
        updateBookmarkIcon(currentFragment ?: HomeFragment())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTag = fragment::class.java.simpleName

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null && currentFragment::class.java.simpleName == fragmentTag) {
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(fragmentTag)
            .commit()

        updateToolbar(fragment)
        updateHomeIcon(fragment)
        updateBookmarkIcon(fragment)
    }

    private fun updateToolbar(fragment: Fragment) {
        // Check the fragment type and adjust the toolbar accordingly
        if (fragment is HomeFragment) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("camera_permission", true).apply()
                Toast.makeText(this, "akses kamera diizinkan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "akses kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val CameraX_Permissions = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
