package com.example.tpueco.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.R
import com.example.tpueco.domain.adapter.MailGroupsAdapter
import com.example.tpueco.presentation.VM.MailMainViewModel

class MailMainFragment : Fragment(), View.OnClickListener {

    lateinit var mailGroupsAdapter: MailGroupsAdapter
    lateinit var mailMainRecyclerView: RecyclerView

    var s: Int = 0

    companion object {
        val groupLive = MutableLiveData<MutableList<MutableList<com.example.tpueco.domain.Model.Message>>>()

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
        mailGroupsAdapter = MailGroupsAdapter(requireContext())
        mailMainRecyclerView = requireView().findViewById(R.id.mailMainRecycler)
        mailMainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mailMainRecyclerView.adapter = mailGroupsAdapter
        getItemTouchHelper().attachToRecyclerView(mailMainRecyclerView)

        activity?.let {
            groupLive.observe(it, Observer {
                    mailGroupsAdapter.updateAdapter(it)

            })
        }


    }


    override fun onResume() {
        super.onResume()


    }

    override fun onClick(p0: View?) {

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

            }
        })
    }


}