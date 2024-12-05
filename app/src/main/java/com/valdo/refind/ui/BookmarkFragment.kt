package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.valdo.refind.R

class BookmarkFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }

}