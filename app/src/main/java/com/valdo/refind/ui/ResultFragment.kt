import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.valdo.refind.R

class ResultFragment : Fragment() {

    private lateinit var resultImage: ImageView
    private lateinit var resultText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        resultImage = view.findViewById(R.id.result_image)
        resultText = view.findViewById(R.id.result_text)

        // Retrieve data from arguments
        val imagePath = arguments?.getString("imageUri")
        val predictionResult = arguments?.getString("predictionResult")

        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(resultImage)
        }

        resultText.text = predictionResult ?: "No prediction result available"

        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_settings)?.isVisible = false
    }

}