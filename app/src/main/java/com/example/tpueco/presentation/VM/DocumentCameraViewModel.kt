package com.example.tpueco.presentation.VM

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.tpueco.domain.tools.camera.YUVtoRGB
import com.google.common.util.concurrent.ListenableFuture


class DocumentCameraViewModel : ViewModel() {

}