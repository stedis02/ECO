package com.example.tpueco.domain.tools.Document

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.*


class DocumentManager {



    fun addPageToDocumentPdf(context: Context , pdfDocument: PdfDocument , pageNumber: Int, bitmap: Bitmap) {
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(3000, 4000, pageNumber).create()
        val page1 = pdfDocument.startPage(pageInfo)
        val canvas = page1.canvas
        val matrix = Matrix()
        matrix.postRotate(90.0f)
        val bitmapRot = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        canvas.drawBitmap(bitmapRot, 0.0f, 0.0f, paint)
        pdfDocument.finishPage(page1)

    }

    fun saveDocumentPdf(context: Context, pdfDocument: PdfDocument, fileName: String){
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
        pdfDocument.writeTo(outputStream)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun deleteFileUsingDisplayName(context: Context, displayName: String) {
        val uri: Uri = getUriFromDisplayName(context, displayName)!!
        val resolver = context.contentResolver
        val selectionArgsPdf = arrayOf(displayName)
        try {
            resolver.delete(
                uri,
                MediaStore.Files.FileColumns.DISPLAY_NAME + "=?",
                selectionArgsPdf
            )

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getUriFromDisplayName(context: Context, displayName: String): Uri? {
        val projection: Array<String> = arrayOf(MediaStore.Files.FileColumns._ID)
        val cursor: Cursor = context.contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI, projection,
            MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", arrayOf(displayName), null
        )!!
        cursor.moveToFirst()
        return if (cursor.getCount() > 0) {
            val columnIndex: Int = cursor.getColumnIndex(projection[0])
            val fileId: Long = cursor.getLong(columnIndex)
            cursor.close()
            Uri.parse(MediaStore.Downloads.EXTERNAL_CONTENT_URI.toString() + "/" + fileId)
        } else {
            null
        }
    }


}


