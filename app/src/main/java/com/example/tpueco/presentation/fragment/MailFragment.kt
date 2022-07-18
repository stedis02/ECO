package com.example.tpueco.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tpueco.R
import com.example.tpueco.presentation.VM.MailViewModel

class MailFragment : Fragment() {

    companion object {
        fun newInstance() = MailFragment()
    }

    private lateinit var viewModel: MailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}