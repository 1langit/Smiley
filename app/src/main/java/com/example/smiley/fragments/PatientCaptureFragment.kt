package com.example.smiley.fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.activities.PatientClassificationActivity
import com.example.smiley.databinding.FragmentPatientCaptureBinding
import com.example.smiley.models.ClassificationResponse
import com.example.smiley.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PatientCaptureFragment : Fragment() {

    private lateinit var binding: FragmentPatientCaptureBinding
    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientCaptureBinding.inflate(layoutInflater)

        with(binding) {
            btnSelect.setOnClickListener {
                showImagePickerDialog()
            }
            btnClassify.setOnClickListener {
                if (imageFile != null) {
                    showLoadingState(true)
                    classifyImage(imageFile!!)
                } else {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    private fun showImagePickerDialog() {
        val items = arrayOf("Gallery", "Camera")
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Image Source")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> galleryLauncher.launch("image/*")
                    1 -> {
                        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePicturePreview.launch(null)
                        } else {
                            requestPermission.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                }
            }
            .create()
        dialog.show()
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.imgTeeth.setImageURI(uri)

            val contentResolver = requireContext().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val file = File.createTempFile("image", ".jpg")
            copyStreamToFile(inputStream!!, file)
            imageFile = file
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())  { bitmap ->
        if (bitmap != null) {
            binding.imgTeeth.setImageBitmap(bitmap)

            val file = File.createTempFile("image", ".jpg")
            val success = bitmapToFile(bitmap, Bitmap.CompressFormat.JPEG, file)

            if (success) {
                imageFile = file
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            takePicturePreview.launch(null)
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        outputFile.outputStream().use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, format: Bitmap.CompressFormat, location: File): Boolean {
        val outputStream = FileOutputStream(location)
        val compressed = bitmap.compress(format, 100, outputStream) // Adjust quality as needed
        outputStream.flush()
        outputStream.close()
        return compressed
    }

    private fun classifyImage(file: File) {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val call = ApiClient.getMlApiInstance().classify(imagePart)
        call.enqueue(object : Callback<ClassificationResponse> {
            override fun onResponse(call: Call<ClassificationResponse>, response: Response<ClassificationResponse>) {
                val result = response.body()
                showResult(file, result!!.classification)
            }

            override fun onFailure(call: Call<ClassificationResponse>, t: Throwable) {
                showResult(file, "An error occured:\n$t")
            }
        })
    }

    private fun showResult(file: File, result: String) {
        val newIntent = Intent(requireContext(), PatientClassificationActivity::class.java)
        newIntent.putExtra("image", file.absolutePath)
        newIntent.putExtra("result", result)
        startActivity(newIntent)
        showLoadingState(false)
    }

    private fun showLoadingState(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                txtHint.text = "Loading..."
                fgOverlay.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                btnSelect.isEnabled = false
                btnClassify.isEnabled = false
            } else {
                txtHint.text = getString(R.string.capture_hint)
                fgOverlay.visibility = View.GONE
                progressBar.visibility = View.GONE
                btnSelect.isEnabled = true
                btnClassify.isEnabled = true
                imgTeeth.setImageResource(R.drawable.placeholder)
            }
        }
    }
}