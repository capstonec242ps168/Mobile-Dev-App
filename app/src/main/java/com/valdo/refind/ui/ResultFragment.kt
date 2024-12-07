package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.valdo.refind.R

class ResultFragment : Fragment() {

    private lateinit var resultImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var craftListButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        resultImage = view.findViewById(R.id.result_image)
        resultText = view.findViewById(R.id.result_text)
        craftListButton = view.findViewById(R.id.btn_craft_result)

        // Retrieve data from arguments
        val imagePath = arguments?.getString("imageUri")
        val predictionResult = arguments?.getString("predictionResult")

        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(resultImage)
        }

        resultText.text = predictionResult ?: "No prediction result available"

        // Handle button click to open the CraftFragment with the predictionResult label
        craftListButton.setOnClickListener {
            predictionResult?.let { endpoint ->
                openCraftFragment(endpoint)
            }
        }

        return view
    }

    private fun openCraftFragment(endpoint: String) {
        // Open the CraftFragment, passing the predictionResult as the label
        val craftFragment = CraftFragment()
        val bundle = Bundle().apply {
            putString("label", endpoint)  // Pass predictionResult as label
        }
        craftFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, craftFragment)
            .addToBackStack("CraftFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_settings)?.isVisible = false
    }
}