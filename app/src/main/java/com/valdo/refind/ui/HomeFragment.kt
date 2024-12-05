package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.valdo.refind.R
import com.valdo.refind.data.remote.ApiConfig

class HomeFragment : Fragment() {

    private val baseUrl = ApiConfig.BASE_URL // Assuming ApiConfig.BASE_URL is declared in your project

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for ImageButtons with dynamic logging
        setButtonClickListener(view, R.id.btnPlastik, "plastic")
        setButtonClickListener(view, R.id.btnKertas, "paper")
        setButtonClickListener(view, R.id.btnKardus, "cardboard")
        setButtonClickListener(view, R.id.btnBaju, "clothes")
        setButtonClickListener(view, R.id.btnKaleng, "metal")
        setButtonClickListener(view, R.id.btnSepatu, "shoes")
        setButtonClickListener(view, R.id.btnOrganik, "biological")
        setButtonClickListener(view, R.id.btnKaca, "white-glass")
    }

    private fun setButtonClickListener(view: View, buttonId: Int, endpoint: String) {
        view.findViewById<View>(buttonId).setOnClickListener {
            logApiCall(endpoint)
            (activity as? MainActivity)?.openCraftFragment(endpoint)
        }
    }

    private fun logApiCall(endpoint: String) {
        val url = "$baseUrl/$endpoint"
        Log.d("HomeFragment", "API called: $url")
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}