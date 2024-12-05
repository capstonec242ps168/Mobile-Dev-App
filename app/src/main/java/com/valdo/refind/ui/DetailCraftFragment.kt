package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.valdo.refind.R
import com.valdo.refind.data.remote.ApiClient
import com.valdo.refind.data.remote.CraftResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCraftFragment : Fragment() {

    private lateinit var craftImage: ImageView
    private lateinit var craftTitle: TextView
    private lateinit var craftStep: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_craft, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the views
        craftImage = view.findViewById(R.id.craft_image)
        craftTitle = view.findViewById(R.id.craft_title)
        craftStep = view.findViewById(R.id.craft_step)

        // Retrieve data from arguments
        val craftName = arguments?.getString("name")
        val craftImageURL = arguments?.getString("image")
        val craftStepDetails = arguments?.getString("step")

        // Set data to views
        craftTitle.text = craftName
        craftStep.text = craftStepDetails

        // Load the image using Glide
        if (!craftImageURL.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(craftImageURL)
                .into(craftImage)
        } else {
            craftImage.setImageResource(R.drawable.protecting_the_environment) // Replace with your placeholder image
        }
    }
}