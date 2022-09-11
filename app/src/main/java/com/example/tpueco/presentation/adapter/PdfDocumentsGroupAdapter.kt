package com.example.tpueco.presentation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.R
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.tools.Document.Document
import java.io.File

class PdfDocumentsGroupAdapter(var context: Context) :
    RecyclerView.Adapter<PdfDocumentsGroupAdapter.MainViewHolder>() {
    var pdfDocumentsGroup: MutableList<Document> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pdf_document_item, parent, false)
        return MainViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setData(pdfDocumentsGroup.get(position).pdfDocumentName)
    }

    override fun getItemCount(): Int {
        return pdfDocumentsGroup.size
    }

    fun updateAdapter(newPdfDocumentsGroup: MutableList<Document>) {
        pdfDocumentsGroup.clear()
        pdfDocumentsGroup.addAll(newPdfDocumentsGroup)
        pdfDocumentsGroup.reverse()
        notifyDataSetChanged()
    }

    fun removeItemFromDataBase(position: Int, dbManager: DBManager) {
        dbManager.deletePdfDocument(pdfDocumentsGroup.get(position).documentId.toString())
    }

    class MainViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var pdfDocumentNameView: TextView = itemView.findViewById(R.id.mailMainHeader)
        private val sendPDFDocument: Button = itemView.findViewById(R.id.sendDocumentButtom)
        var context: Context
        lateinit var pdfDocumentName: String

        init {
            sendPDFDocument.setOnClickListener(this)
            this.context = context
        }

        fun setData(pdfDocumentName: String) {
            pdfDocumentNameView.text = pdfDocumentName
            this.pdfDocumentName = pdfDocumentName
        }


        override fun onClick(p0: View?) {
            sharePDFDocument(context, pdfDocumentName)
        }

        private fun sharePDFDocument(context: Context, pdfDocumentName: String) {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + pdfDocumentName
            )

            var pdfUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName.toString() + ".provider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            val share = Intent()
            share.action = Intent.ACTION_SEND
            share.type = "application/pdf"
            share.putExtra(Intent.EXTRA_STREAM, pdfUri)
            context.startActivity(Intent.createChooser(share, "Share file"))
        }
    }
}