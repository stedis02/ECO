package com.example.tpueco.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.databinding.ActivityCameraBinding
import com.example.tpueco.databinding.ActivityMainBinding
import com.example.tpueco.presentation.VM.StartBrowserViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

    class CameraActivity : AppCompatActivity() {

        private lateinit var viewBinding: ActivityCameraBinding

        private var imageCapture: ImageCapture? = null



        private lateinit var cameraExecutor: ExecutorService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewBinding = ActivityCameraBinding.inflate(layoutInflater)
            setContentView(viewBinding.root)

            // Request camera permissions
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }

            // Set up the listeners for take photo and video capture buttons
          //  viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }

            cameraExecutor = Executors.newSingleThreadExecutor()
        }

        private fun takePhoto() {}



        private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                    }

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview)

                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(this))


        }

        private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

        override fun onDestroy() {
            super.onDestroy()
            cameraExecutor.shutdown()
        }

        companion object {
            private const val TAG = "CameraXApp"
            private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
            private const val REQUEST_CODE_PERMISSIONS = 10
            private val REQUIRED_PERMISSIONS =
                mutableListOf (
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ).apply {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }.toTypedArray()
        }

    }