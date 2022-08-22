package com.example.tpueco.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.R
import com.example.tpueco.domain.Model.Message
import com.example.tpueco.presentation.adapter.MailGroupsAdapter
import com.example.tpueco.presentation.VM.MailMainViewModel

class MailMainFragment : Fragment() {

    lateinit var mailGroupsAdapter: MailGroupsAdapter
    lateinit var mailMainRecyclerView: RecyclerView


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var cameraProgressBar: ProgressBar
        val groupLive = MutableLiveData<MutableList<MutableList<Message>>>()
        fun newInstance() = MailMainFragment()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<MailMainViewModel>()
            .documentFeatureComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cameraProgressBar = requireView().findViewById(R.id.MailProgressBar)
        cameraProgressBar.visibility = View.INVISIBLE
        mailGroupsAdapter = MailGroupsAdapter(requireContext())
        mailMainRecyclerView = requireView().findViewById(R.id.mailMainRecycler)
        mailMainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mailMainRecyclerView.adapter = mailGroupsAdapter
        activity?.let {
            groupLive.observe(it, Observer {
                mailGroupsAdapter.updateAdapter(it)

            })
        }


    }


}