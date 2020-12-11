package com.example.myapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.confirmit.mobilesdk.ConfirmitServer
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentTriggerListBinding
import com.example.myapp.domain.TriggerManager
import com.example.myapp.fragments.viewmodels.TriggerListViewModel
import com.example.myapp.fragments.viewmodels.models.ProgramModel
import com.example.myapp.utils.hideKeyboard
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

interface OnTriggerCardViewClicked {
    fun onCardClicked(program: ProgramModel)
}

class TriggerListFragment : Fragment(), OnTriggerCardViewClicked, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnProgramAddedListener {
    companion object {
        fun newInstance(): TriggerListFragment {
            return TriggerListFragment()
        }
    }

    private lateinit var viewModel: TriggerListViewModel
    private lateinit var triggerListAdapter: TriggerListAdapter

    private var _binding: FragmentTriggerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTriggerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hideKeyboard()

        viewModel = ViewModelProvider(this).get(TriggerListViewModel::class.java)

        binding.mainFab.setImageResource(R.drawable.ic_add_black)
        binding.mainFab.setOnClickListener(this)

        triggerListAdapter = TriggerListAdapter(viewModel, this)

        binding.triggerListNoProgram.visibility = ConstraintLayout.GONE
        binding.triggerListTable.adapter = triggerListAdapter
        binding.triggerListTable.layoutManager = LinearLayoutManager(context)
        binding.triggerListSwipeRefresh.setOnRefreshListener(this)
        binding.triggerListSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        refreshTable()
    }

    override fun onRefresh() {
        downloadAll()
    }

    override fun onCardClicked(program: ProgramModel) {
        (activity as? MainActivity)?.navigateToOverview(program.program)
    }

    override fun onClick(view: View?) {
        val fragment = AddProgramFragment()
        fragment.listener = this
        fragment.show(childFragmentManager, "AddProgramFragment")
    }

    private fun downloadAll() {
        viewModel.viewModelScope.launch {
            viewModel.downloadAllPrograms()
            refreshTable()
            binding.triggerListSwipeRefresh.isRefreshing = false
        }
    }

    override fun onProgramAdded(serverId: String, programKey: String) {
        TriggerManager().setDelegate(serverId, programKey)
        viewModel.viewModelScope.launch {
            viewModel.downloadProgram(serverId, programKey)
            binding.triggerListNoProgram.visibility = if (viewModel.programs.isNotEmpty()) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            triggerListAdapter.notifyDataSetChanged()
        }

    }

    private fun refreshTable() {
        binding.triggerListNoProgram.visibility = if (viewModel.programs.isNotEmpty()) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
        triggerListAdapter.notifyDataSetChanged()
    }

    private class ViewHolder(view: View, private val cardClickListener: OnTriggerCardViewClicked) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var program: ProgramModel
        private val card: MaterialCardView = view.findViewById(R.id.triggerListCard)
        private val txtTitle: TextView = view.findViewById(R.id.triggerListCardTitle)

        init {
            card.isClickable = true
            card.setOnClickListener(this)
        }

        fun setup(program: ProgramModel) {
            this.program = program

            var serverName = ""
            ConfirmitServer.getServer(program.configs.serverId)?.let {
                val split = it.name.split("-")
                serverName = "${split[0]} - "
            }

            txtTitle.text = "$serverName${program.program.programKey}"
        }

        override fun onClick(view: View?) {
            if (view == card) {
                cardClickListener.onCardClicked(program)
            }
        }
    }

    private class TriggerListAdapter(private val viewModel: TriggerListViewModel, private val cardClickListner: OnTriggerCardViewClicked) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.app_card_trigger, parent, false)
            return ViewHolder(view, cardClickListner)
        }

        override fun getItemCount(): Int {
            return viewModel.programs.count()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setup(viewModel.programs[position])
        }
    }
}