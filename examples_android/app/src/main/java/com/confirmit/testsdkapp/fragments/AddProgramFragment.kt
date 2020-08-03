package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.utils.ServerManager
import kotlinx.android.synthetic.main.fragment_add_program.*

class AddProgramFragment : BaseFragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    override val layoutId: Int = R.layout.fragment_add_program

    private val servers = ServerManager.getDefaultServers()
    private val serverNameList: List<String> = ServerManager.getDefaultServers().map { x -> x.name }
    private var selectedServerPos: Int = 0

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        addProgramDone.setOnClickListener(this)

        val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, serverNameList)
        addProgramHost.adapter = adapter
        addProgramHost.onItemSelectedListener = this
        addProgramHost.setSelection(selectedServerPos)

        (activity as MainActivity).hideFab()
    }

    override fun onClick(p0: View?) {
        hideKeyboard()
        val programKey = addProgramKey.text.toString()
        val serverId = servers[selectedServerPos].serverId

        val customClientId = txtClientId.text.toString()
        val customClientSecret = txtClientSecret.text.toString()

        val navController = Navigation.findNavController(view!!)
        navController.popBackStack()

        val bundle = TriggerListFragmentArgs.Builder()
                .setNewTriggerProgramKey(programKey)
                .setNewTriggerServerId(serverId)
                .setNewTriggerClientId(customClientId)
                .setNewTriggerClientSecret(customClientSecret)
                .build()
                .toBundle()
        navController.navigate(R.id.actionRootToTriggerList, bundle)

        hideKeyboard()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedServerPos = position
        txtClientId.text.clear()
        txtClientSecret.text.clear()

        ServerManager.getServerPref(selectedServerPos)?.let {
            txtClientId.setText(it.clientId)
            txtClientSecret.setText(it.clientSecret)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}