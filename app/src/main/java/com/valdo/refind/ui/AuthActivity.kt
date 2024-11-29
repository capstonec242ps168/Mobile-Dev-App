package com.valdo.refind.ui

import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.valdo.refind.R
import com.valdo.refind.adapter.SectionsPagerAdapter

class AuthActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,  // "Masuk" tab
            R.string.tab_text_2   // "Daftar" tab
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Set up UI always in light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        imageView = findViewById(R.id.imageView)  // ImageView to change based on tab
        textView = findViewById(R.id.textWelcome)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabLayout)
        // Linking ViewPager2 with TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        imageView.setColorFilter(R.color.banner)


        // Set up tab selection listener to change image
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // When a tab is selected, change the image accordingly
                when (tab?.position) {
                    0 -> {
                        // "Masuk" tab selected
                        imageView.setImageResource(R.drawable.banner1)  // Set image for "Masuk"
                        textView.text = getString(R.string.banner_login)
                    }
                    1 -> {
                        // "Daftar" tab selected
                        imageView.setImageResource(R.drawable.banner2)  // Set image for "Daftar"
                        textView.text = getString(R.string.banner_register)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Optional: Handle tab unselection if needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional: Handle tab reselection if needed
            }
        })

        // Optional: Remove ActionBar elevation for a cleaner look
        supportActionBar?.elevation = 0f
    }
}
