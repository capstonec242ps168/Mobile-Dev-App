import android.os.Bundle
import android.view.LayoutInflater
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        resultImage = view.findViewById(R.id.result_image)
        resultText = view.findViewById(R.id.result_text)

        // Get the image path from the arguments
        val imagePath = arguments?.getString("imageUri")
        imagePath?.let {
            // Use Glide or Picasso to load the image
            Glide.with(this)
                .load(File(it))  // Load the image from the file path
                .into(resultImage)
        }

        return view
    }
}
