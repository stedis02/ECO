package com.example.tpueco.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tpueco.MainActivity
import com.example.tpueco.databinding.ActivityCameraBinding
import com.example.tpueco.domain.tools.Document.DocumentManager
import java.nio.ByteBuffer
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    val pdfDocument = PdfDocument()
    private var photoСounter = 1
    var pdfDocumentPageNumber = 1
    var documentManager = DocumentManager()
    lateinit var cameraProgressBar: ProgressBar
    private val executor = Executors.newSingleThreadExecutor()
    val pdfDocumentReadyStatus = MutableLiveData<Int>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        cameraProgressBar = viewBinding.cameraProgressBar
        setContentView(viewBinding.root)
        cameraProgressBar.visibility = View.INVISIBLE
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

    }

    // buttons
    fun takePhoto(view: View) {
        addingPhotoAsDocumentPage()

    }

    fun saveDocument(view: View) {
        getAllButtonLock(false)
        pdfDocumentReadyStatus.observe(this, Observer {
            if (it != photoСounter) {
                cameraProgressBar.visibility = View.VISIBLE

            } else {
                cameraProgressBar.visibility = View.INVISIBLE
                documentManager.saveDocumentPdf(
                    applicationContext,
                    pdfDocument,
                    getEnteredPDFDocumentName()
                )
                Toast.makeText(applicationContext, "Файл сохранён!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@CameraActivity)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().setJpegQuality(10).build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this@CameraActivity, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAGCamera, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun addingPhotoAsDocumentPage() {
        val imageCapture = imageCapture ?: return
        photoСounter++
        imageCapture.takePicture(executor, object :
            ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = imageProxyToBitmap(image)
                image.close()
                documentManager.addPageToDocumentPdf(
                    applicationContext,
                    pdfDocument,
                    pdfDocumentPageNumber,
                    bitmap
                )
                pdfDocumentPageNumber++
                Log.v(TAGCameraDocument, "Photo added to document on page ${pdfDocumentPageNumber}")
                pdfDocumentReadyStatus.postValue(pdfDocumentPageNumber)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.v(TAGCameraDocument, "For some reason the photo was not added to the document")
            }

        })

    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getAllButtonLock(lockStatus: Boolean) {
        viewBinding.imageCaptureButton.isEnabled = lockStatus
        viewBinding.documentSaveButton.isEnabled = lockStatus
    }

    fun getEnteredPDFDocumentName(): String {
        var pdfDocumentFileName: String = intent.getStringExtra("pdfDocumentFileName").toString()
        return "${pdfDocumentFileName}.pdf"
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.isShutdown
        pdfDocument.close()
    }

    companion object {
        private const val TAGCamera = "Camera"
        private const val TAGCameraDocument = "CameraDocument"
        private const val REQUEST_CODE_PERMISSIONS = 11
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()


    }
}