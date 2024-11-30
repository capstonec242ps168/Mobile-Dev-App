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
import java.io.File

class ResultFragment : Fragment() {

    private lateinit var resultImage: ImageView
    private lateinit var resultText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        resultImage = view.findViewById(R.id.result_image)
        resultText = view.findViewById(R.id.result_text)

        // Get the image path or URI from the arguments
        val imagePath = arguments?.getString("imageUri")
        if (imagePath == null) {
            resultText.text = "No image to display"
        } else {
            // Load the image with Glide
            Glide.with(this)
                .load(imagePath) // Handles both URI and File paths
//              .placeholder(R.drawable.loading_placeholder) // Show while loading
//              .error(R.drawable.error_placeholder) // Show on failure
                .into(resultImage)
        }

        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.action_settings)?.isVisible = false
    }

}
