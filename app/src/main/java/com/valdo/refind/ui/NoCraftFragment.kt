package com.valdo.refind.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.valdo.refind.R

class NoCraftFragment : Fragment() {

    private lateinit var resultImage: ImageView
    private lateinit var warningText: TextView
    private lateinit var treatmentText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_craft, container, false)

        resultImage = view.findViewById(R.id.result_image)
        warningText = view.findViewById(R.id.warning)
        treatmentText = view.findViewById(R.id.treatment)

        val imageResId = arguments?.getInt("imageResId", 0)
        val imagePath = arguments?.getString("imageUri")
        val predictionResult = arguments?.getString("predictionResult")
        val warning = arguments?.getString("warning")
        val treatment = arguments?.getString("treatment")

        if (imageResId != null && imageResId != 0) {
            resultImage.setImageResource(imageResId)
        } else if (!imagePath.isNullOrEmpty()) {
            Glide.with(this)
                .load(imagePath)
                .into(resultImage)
        }

        if (!warning.isNullOrEmpty() && !treatment.isNullOrEmpty()) {
            warningText.text = warning
            treatmentText.text = treatment
        } else {
            when (predictionResult) {
                "biological" -> {
                    warningText.text = getString(R.string.warning_bio)
                    treatmentText.text = getString(R.string.treatment_bio)
                }
                else -> {
                    warningText.text = getString(R.string.warning_no_craft)
                    treatmentText.text = getString(R.string.treatment_no_craft)
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE

    }
}