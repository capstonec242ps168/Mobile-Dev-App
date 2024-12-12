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
        resultText = view.findViewById(R.id.predict_result)
        craftListButton = view.findViewById(R.id.btn_craft_result)

        val imagePath = arguments?.getString("imageUri")
        val predictionResult = arguments?.getString("predictionResult")

        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(resultImage)
        }

        val displayText = when (predictionResult) {
            "plastic" -> getString(R.string.plastic)
            "metal" -> getString(R.string.metal)
            "paper" -> getString(R.string.paper)
            "cardboard" -> getString(R.string.cardboard)
            "clothes" -> getString(R.string.clothe)
            "shoe" -> getString(R.string.shoe)
            "white-glass" -> getString(R.string.glass)
            "green-glass" -> getString(R.string.glass)
            "brown-glass" -> getString(R.string.glass)
            "biological" -> getString(R.string.biological)
            "battery"-> getString(R.string.battery)
            "trash"-> getString(R.string.trash)
            else -> "yo ndak tau"
        }

        resultText.text = displayText

        craftListButton.setOnClickListener {
            predictionResult?.let { endpoint ->
                openCraftFragment(endpoint)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }

    private fun openCraftFragment(endpoint: String) {
        val craftFragment = CraftFragment()
        val bundle = Bundle().apply {
            putString("label", endpoint)
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
        menu.findItem(R.id.action_settings)?.isVisible = false
    }
}