package com.valdo.refind.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.valdo.refind.R
import com.valdo.refind.data.remote.ApiClient
import com.valdo.refind.data.remote.PredictResponse
import com.valdo.refind.helper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var previewView: PreviewView
    private lateinit var galleryButton: MaterialButton
    private lateinit var takePictureButton: MaterialButton
    private lateinit var cameraSwitchButton: MaterialButton
    private lateinit var imageCapture: androidx.camera.core.ImageCapture

    private var isUsingBackCamera: Boolean = true

    companion object {
        private const val REQUEST_CODE_GALLERY = 100
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)

        // Initialize views
        progressIndicator = view.findViewById(R.id.progressIndicator)
        previewView = view.findViewById(R.id.previewCam)
        galleryButton = view.findViewById(R.id.gallery)
        takePictureButton = view.findViewById(R.id.takePicture)
        cameraSwitchButton = view.findViewById(R.id.camera_switch)

        checkPermissions()
        setupButtons()

        return view
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.apply {
            show() // Make sure the ActionBar is visible
            setDisplayHomeAsUpEnabled(true) // Show the back button
        }

        // Optionally hide other views like Bottom Navigation, FAB, etc.
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            setupCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera()
            } else {
                Toast.makeText(requireContext(), "Tidak ada akses kamera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = if (isUsingBackCamera) {
                androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
            }

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = androidx.camera.core.ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal mengakses kamera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupButtons() {
        galleryButton.setOnClickListener {
            // Open the gallery
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }

        takePictureButton.setOnClickListener { takePicture() }

        cameraSwitchButton.setOnClickListener {
            // Switch the camera
            isUsingBackCamera = !isUsingBackCamera
            setupCamera() // Reinitialize the camera with the updated selector
        }
    }

    private fun takePicture() {
        if (!::imageCapture.isInitialized) {
            Toast.makeText(requireContext(), "Tidak ada akses kamera", Toast.LENGTH_SHORT).show()
            return
        }

        saveImageToMediaStore(imageCapture, requireContext())

        // Save to app-specific storage
        val photoFile = File(requireContext().getExternalFilesDir(null), "photo_${System.currentTimeMillis()}.jpg")
        val outputOptions = androidx.camera.core.ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : androidx.camera.core.ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: androidx.camera.core.ImageCapture.OutputFileResults) {

                    uploadImage(photoFile)

                    // Pass the file URI to the com.valdo.refind.ui.ResultFragment
                    val resultFragment = ResultFragment()
                    val bundle = Bundle()
                    bundle.putString("imageUri", photoFile.absolutePath)  // Pass the image file path or URI
                    resultFragment.arguments = bundle


                }

                override fun onError(exception: ImageCaptureException) {
                }
            }
        )
    }

    private fun uploadImage(file: File) {
        progressIndicator.visibility = View.VISIBLE
        val reducedFile = file.reduceFileImage()

        Log.d("ScanFragment", "Image reduced to: ${reducedFile.length()} bytes")

        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull()) // Define the file type
        val part = MultipartBody.Part.createFormData("image", reducedFile.name, requestBody)

        ApiClient.apiService.postPrediction(part).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {

                progressIndicator.visibility = View.GONE

                if (response.isSuccessful) {
                    val predictionResult = response.body()?.data?.result

                    val targetFragment = when (predictionResult) {
                        "battery", "biological", "trash" -> NoCraftFragment()
                        else -> ResultFragment()
                    }

                    val bundle = Bundle().apply {
                        putString("predictionResult", predictionResult)
                        putString("imageUri", file.absolutePath)
                    }
                    targetFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, targetFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()

                } else {
                    Log.e("ScanFragment", "API call failed: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.e("ScanFragment", "API call error: ${t.message}")
            }
        })
    }


    fun saveImageToMediaStore(imageCapture: ImageCapture, context: Context) {
        // Step 1: Create ContentValues with image metadata
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg") // Image name
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Mime type
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Refind") // Save to Pictures/Refind directory
        }

        val resolver = context.contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Step 2: Check if URI is valid
        if (imageUri != null) {
            // Step 3: Open an OutputStream to the URI for writing the image
            val outputStream: OutputStream? = resolver.openOutputStream(imageUri)

            // Step 4: Capture the image and save it
            imageCapture.takePicture(
                ImageCapture.OutputFileOptions.Builder(outputStream!!).build(),
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                        // Step 5: Force a media scan after saving the image
                        context.sendBroadcast(
                            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri)
                        )
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(context, "Gagal menyimpan gambar: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            Toast.makeText(context, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val file = File(getRealPathFromURI(uri))  // Convert URI to File
                uploadImageFromUri(uri)

                // Pass the URI to com.valdo.refind.ui.ResultFragment
                val resultFragment = ResultFragment()
                val bundle = Bundle()
                bundle.putString("imageUri", uri.toString()) // Use the image URI as a string
                resultFragment.arguments = bundle

            }
        }
    }

    private fun uploadImageFromUri(uri: Uri) {
        // You can use the URI directly for upload instead of converting to a file path.
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File.createTempFile("temp_image", ".jpg", requireContext().cacheDir)
        inputStream?.copyTo(file.outputStream())
        uploadImage(file) // Upload the image file after creating it from the URI
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var cursor: android.database.Cursor? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        try {
            // Query the MediaStore for the actual path of the file
            cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return cursor?.getString(columnIndex ?: -1) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } finally {
            cursor?.close()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Remove specific menu items by ID
        menu.findItem(R.id.action_settings)?.isVisible = false
    }


    override fun onDestroy() {
        super.onDestroy()
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.apply {
            show() // Ensure the toolbar is shown again
            setDisplayHomeAsUpEnabled(false) // Hide the back button when leaving the fragment
            setDisplayShowTitleEnabled(true) // Show title if needed
        }

        // Restore visibility of other views
        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}