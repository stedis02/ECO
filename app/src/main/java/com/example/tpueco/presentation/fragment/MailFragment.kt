package com.example.tpueco.presentation.fragment

import android.R.attr.defaultValue
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.domain.adapter.MailAdapter
import com.example.tpueco.domain.adapter.MailGroupsAdapter
import com.example.tpueco.presentation.VM.MailViewModel


class MailFragment : Fragment() {


    companion object {
        fun newInstance() = MailFragment()
    }

    private lateinit var viewModel: MailViewModel
    lateinit var mailAdapter: MailAdapter
    lateinit var mailRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MailViewModel::class.java)

        val bundle = this.arguments
        val pos = bundle!!.getInt("position", defaultValue)
        mailAdapter = MailAdapter(requireContext())
        mailRecyclerView = requireView().findViewById(R.id.mailRecyclerView)
        mailRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mailRecyclerView.adapter = mailAdapter
        mailAdapter.updateAdapter(MainActivity.messageGroups.get(pos))
    }

}