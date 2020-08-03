package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.domain.TriggerManager
import com.confirmit.testsdkapp.viewmodels.TriggerListViewModel
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel
import kotlinx.android.synthetic.main.fragment_trigger_list.*

class TriggerListFragment : BaseFragment(), OnTriggerCardViewClicked, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    override var layoutId: Int = R.layout.fragment_trigger_list

    private lateinit var viewModel: TriggerListViewModel
    private lateinit var triggerListAdapter: TriggerListAdapter

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        hideKeyboard()

        viewModel = TriggerListViewModel(activity!!)

        val mainActivity = activity as MainActivity
        val fab = mainActivity.showFab()
        fab.setImageResource(R.drawable.ic_add_black)
        fab.setOnClickListener(this)

        triggerListAdapter = TriggerListAdapter(viewModel, this)

        triggerListNoProgram.visibility = ConstraintLayout.GONE
        triggerListTable.adapter = triggerListAdapter
        triggerListTable.layoutManager = LinearLayoutManager(context)
        triggerListSwipeRefresh.setOnRefreshListener(this)
        triggerListSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        refreshTable()

        handleNewPrograms()
    }

    override fun onRefresh() {
        downloadAll()
    }

    override fun onCardClicked(program: ProgramModel) {
        findNavController().navigate(TriggerListFragmentDirections.actionTriggerListToOverview(program))
    }

    override fun onClick(view: View?) {
        findNavController().navigate(TriggerListFragmentDirections.actionTriggerListToAddProgram())
    }

    private fun downloadAll() {
        viewModel.downloadAllProgramsAsync {
            postUi {
                refreshTable()
                triggerListSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun handleNewPrograms() {
        if (arguments == null) {
            return
        }

        val bundle = TriggerListFragmentArgs.fromBundle(arguments!!)
        var serverId = bundle.newTriggerServerId
        val programKey = bundle.newTriggerProgramKey
        val clientId = bundle.newTriggerClientId
        val clientSecret = bundle.newTriggerClientSecret
        if (serverId.isBlank() || programKey.isBlank()) {
            return
        }

        if (clientId.isNotEmpty() && clientSecret.isNotEmpty()) {
            serverId = viewModel.setNewServer(serverId, clientId, clientSecret).serverId
        }

        arguments!!.putString("newTriggerServerId", "")
        arguments!!.putString("newTriggerProgramKey", "")
        arguments!!.putString("newTriggerClientId", "")
        arguments!!.putString("newTriggerClientSecret", "")
        TriggerManager().setDelegate(serverId, programKey)
        (activity as MainActivity).fetcherListener?.startIdle()
        viewModel.downloadProgramAsync(serverId, programKey) {
            postUi {
                refreshTable {
                    (activity as MainActivity).fetcherListener?.endIdle()
                }
            }
        }
    }

    private fun refreshTable(onCompletion: (() -> Unit)? = null) {
        postUi {
            triggerListNoProgram.visibility = if (viewModel.programs.isNotEmpty()) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            triggerListAdapter.notifyDataSetChanged()
            onCompletion?.invoke()
        }
    }
}