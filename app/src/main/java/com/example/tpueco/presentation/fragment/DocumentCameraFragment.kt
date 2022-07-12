package com.example.tpueco.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.R
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.adapter.PdfDocumentsGroupAdapter
import com.example.tpueco.domain.tools.Document.DocumentManager
import com.example.tpueco.presentation.CameraActivity
import com.example.tpueco.presentation.VM.DocumentCameraViewModel


class DocumentCameraFragment : Fragment(), View.OnClickListener {
    lateinit var documentManager: DocumentManager
    lateinit var pdfDocumentFileName: EditText
    lateinit var pdfDocumentRecyclerView: RecyclerView
    lateinit var pdfDocumentsGroupAdapter: PdfDocumentsGroupAdapter
    lateinit var dbManager: DBManager

    companion object {
        fun newInstance() = DocumentCameraFragment()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<DocumentCameraViewModel>()
            .documentFeatureComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document_camera, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbManager = DBManager(requireContext())
        dbManager.dbOpen()
        documentManager = DocumentManager()
        pdfDocumentFileName = requireView().findViewById<EditText>(R.id.pdfDocumentNameId)
        pdfDocumentsGroupAdapter = PdfDocumentsGroupAdapter(requireContext())
        pdfDocumentRecyclerView = requireView().findViewById(R.id.DocumentRecycler)
        pdfDocumentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        pdfDocumentRecyclerView.adapter = pdfDocumentsGroupAdapter
        pdfDocumentsGroupAdapter.updateAdapter(dbManager.getPdfDocument())
        getItemTouchHelper().attachToRecyclerView(pdfDocumentRecyclerView)
        val openCamera: Button = requireView().findViewById(R.id.openCamera)
        openCamera.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        if (translateButtonIdToIndex(p0!!.id) == 1) {
            if (pdfDocumentFileName.text.toString() == "введите имя нового документа"
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

    private fun translateButtonIdToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.openCamera -> index = 1

        }
        return index
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                @NonNull recyclerView: RecyclerView,
                @NonNull viewHolder: RecyclerView.ViewHolder,
                @NonNull target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletePDFDocumentFromDataBaseAndStorage(viewHolder)
                pdfDocumentsGroupAdapter.updateAdapter(dbManager.getPdfDocument())
            }
        })
    }

    fun deletePDFDocumentFromDataBaseAndStorage(viewHolder: RecyclerView.ViewHolder) {
        documentManager.deleteFileUsingDisplayName(
            requireContext(),
            pdfDocumentsGroupAdapter.pdfDocumentsGroup.get(viewHolder.adapterPosition).pdfDocumentName
        )
        pdfDocumentsGroupAdapter.removeItemFromDataBase(viewHolder.adapterPosition, dbManager)
    }


}
