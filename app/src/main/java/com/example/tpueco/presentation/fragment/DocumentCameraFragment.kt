package com.example.tpueco.presentation.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tpueco.R
import com.example.tpueco.domain.tools.Document.DocumentManager
import com.example.tpueco.domain.tools.camera.YUVtoRGB
import com.example.tpueco.presentation.CameraActivity
import com.example.tpueco.presentation.VM.DocumentCameraViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService


class DocumentCameraFragment : Fragment(), View.OnClickListener {
    lateinit var pdfDocumentFileName: EditText


    companion object {
        fun newInstance() = DocumentCameraFragment()
    }

    private lateinit var viewModel: DocumentCameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document_camera, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DocumentCameraViewModel::class.java)
        pdfDocumentFileName = requireView().findViewById<EditText>(R.id.pdfDocumentName)
        val openCamera: Button = requireView().findViewById(R.id.openCamera)
        openCamera.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {

        if (translateIdToIndex(p0!!.id) == 1) {
            if (pdfDocumentFileName.text.toString() =="введите имя нового документа"
                || pdfDocumentFileName.text.toString() == ""
            ) {
                Toast.makeText(requireContext(), "Введите имя файла!", Toast.LENGTH_SHORT).show()

            } else {
                var intent = Intent(activity, CameraActivity::class.java)
                intent.putExtra("pdfDocumentFileName", pdfDocumentFileName.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun translateIdToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.openCamera -> index = 1

        }
        return index
    }

}
