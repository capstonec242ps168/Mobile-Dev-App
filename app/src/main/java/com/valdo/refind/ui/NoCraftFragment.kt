package com.valdo.refind.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        // Retrieve the imageUri from the arguments
        val imagePath = arguments?.getString("imageUri")

        // Load the image into the ImageView using Glide
        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(resultImage)
        }

        // You can customize the warning and treatment message based on the result
        warningText.text = "Sayangnya barang tersebut mengandung bahan berbahaya untuk diolah"
        treatmentText.text = "Pastikan barang tersebut dibuang dengan aman, seperti membungkusnya rapat dalam kantong plastik dan membuangnya ke tempat sampah biasa"

        return view
    }
}