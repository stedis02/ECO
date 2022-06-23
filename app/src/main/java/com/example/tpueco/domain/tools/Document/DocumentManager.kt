package com.example.tpueco.domain.tools.Document

import android.R
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.FileOutputStream
import java.io.IOException
import java.io.*

class DocumentManager {
    fun generatePdf(context: Context ,bitmap: Bitmap) {
        val  pdfDoc = PdfDocument()
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(4000, 3000, 1).create()
        var page1 = pdfDoc.startPage(pageInfo)
        var canvas = page1.canvas
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint)
        pdfDoc.finishPage(page1)
        val fileName = "TestFile.pdf"
        val outputStream = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(directory, fileName)
            FileOutputStream(file)
        } else {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "images/*")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)?.let {
                resolver.openOutputStream(it)
            }
        }
        // outputStream?.use { stream ->
        //     stream.write("DDD".encodeToByteArray())
        // }
        pdfDoc.writeTo(outputStream)
        pdfDoc.close()
    }
}


