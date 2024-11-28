package com.valdo.refind.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.valdo.refind.ui.LoginFragment
import com.valdo.refind.ui.RegisterFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LoginFragment()
            1 -> fragment = RegisterFragment()
        }
        return fragment as Fragment
    }
}