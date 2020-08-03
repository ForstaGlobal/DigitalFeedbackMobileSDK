package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.utils.ServerManager
import kotlinx.android.synthetic.main.fragment_add_survey.*

class AddSurveyFragment : BaseFragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    override var layoutId: Int = R.layout.fragment_add_survey

    private val servers = ServerManager.getDefaultServers()
    private val serverNameList: List<String> = ServerManager.getDefaultServers().map { x -> x.name }
    private var selectedServerPos: Int = 0

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        addSurveyDone.setOnClickListener(this)

        val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, serverNameList)
        addSurveyHost.adapter = adapter
        addSurveyHost.onItemSelectedListener = this
        addSurveyHost.setSelection(selectedServerPos)

        (activity as MainActivity).hideFab()
    }

    override fun onClick(v: View?) {
        hideKeyboard()
        val surveyId = addSurveySurveyId.text.toString()
        val serverId = servers[selectedServerPos].serverId

        val customClientId = txtClientId.text.toString()
        val customClientSecret = txtClientSecret.text.toString()

        val navController = Navigation.findNavController(view!!)
        navController.popBackStack()

        val bundle = SurveyListFragmentArgs.Builder()
                .setNewSurveyServerId(serverId)
                .setNewSurveySurveyId(surveyId)
                .setNewTriggerClientId(customClientId)
                .setNewTriggerClientSecret(customClientSecret)
                .build()
                .toBundle()
        navController.navigate(R.id.actionRootToSurveyList, bundle)

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

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}