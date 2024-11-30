package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.valdo.refind.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for ImageButtons
        view.findViewById<View>(R.id.btnPlastik).setOnClickListener {
            openCraftFragment()
        }

        view.findViewById<View>(R.id.btnKertas).setOnClickListener {
            openCraftFragment()
        }

        view.findViewById<View>(R.id.btnKardus).setOnClickListener {
            openCraftFragment()
        }

        view.findViewById<View>(R.id.btnBaju).setOnClickListener {
            openCraftFragment()
        }

        view.findViewById<View>(R.id.btnKaleng).setOnClickListener {
            openCraftFragment()
        }
    }

    private fun openCraftFragment() {
        val craftFragment = CraftFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, craftFragment)
            .addToBackStack(null)
            //transisi animasi
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}
