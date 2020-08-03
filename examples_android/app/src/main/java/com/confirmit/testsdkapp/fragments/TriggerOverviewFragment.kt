package com.confirmit.testsdkapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.utils.Utils
import com.confirmit.testsdkapp.viewmodels.TriggerOverviewViewModel
import kotlinx.android.synthetic.main.fragment_trigger_overview.*

enum class ProgramConfigType {
    ON_EVENT_TRIGGER,
    CUSTOM_DATA,
    REMOVE_PROGRAM,
    ALLOW_OVERLAY
}

class TriggerOverviewFragment : BaseFragment(), OnTriggerOverviewItemClicked, SwipeRefreshLayout.OnRefreshListener {
    companion object {
        private const val APP_PERMISSIONS = 100
    }

    override val layoutId: Int = R.layout.fragment_trigger_overview

    private lateinit var overviewAdapter: TriggerOverviewAdapter
    private lateinit var viewModel: TriggerOverviewViewModel

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        // Unload program information
        val bundle = TriggerOverviewFragmentArgs.fromBundle(arguments!!)
        val program = bundle.programModel.program
        viewModel = TriggerOverviewViewModel(program)

        overviewAdapter = TriggerOverviewAdapter((activity as MainActivity), viewModel, this)
        triggerOverviewTable.adapter = overviewAdapter
        triggerOverviewTable.layoutManager = LinearLayoutManager(context)

        triggerOverviewSwipeRefresh.setOnRefreshListener(this)
        triggerOverviewSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        (activity as MainActivity).hideFab()
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadCountersAsync()
    }

    override fun onConfigClicked(config: ProgramConfigType) {
        val context = this.requireContext()
        when (config) {
            ProgramConfigType.ON_EVENT_TRIGGER -> {
                viewModel.notifyEvent("onTestEvent", viewModel.program.getCustomData())
            }
            ProgramConfigType.CUSTOM_DATA -> {
                val input = EditText(context)
                input.hint = "eg. <key>=<value>;"
                input.setSingleLine(false)
                input.setText(viewModel.program.configs.customData, TextView.BufferType.EDITABLE)

                val lp = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                val margin = Utils.toPx(context, 16)
                lp.setMargins(margin, 0, margin, 0)
                input.layoutParams = lp

                val container = LinearLayout(context)
                container.addView(input)

                AlertDialog.Builder(context)
                        .setTitle("Enter Custom Data Values")
                        .setView(container)
                        .setPositiveButton("OK") { _, _ ->
                            viewModel.program.configs.customData = input.text.toString()
                            overviewAdapter.notifyDataSetChanged()
                        }
                        .show()
            }
            ProgramConfigType.REMOVE_PROGRAM -> {
                if (viewModel.removeProgram()) {
                    findNavController().popBackStack()
                }
            }
            ProgramConfigType.ALLOW_OVERLAY -> {
                requestOverlayPermission()
                AppConfigs.enabledOverlayProgram = if (AppConfigs.enabledOverlayProgram == viewModel.program.program.programKey) {
                    ""
                } else {
                    viewModel.program.program.programKey
                }
                overviewAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onRefresh() {
        viewModel.downloadProgramAsync {
            triggerOverviewSwipeRefresh.isRefreshing = false
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        if (!Settings.canDrawOverlays(requireContext())) {
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            myIntent.data = Uri.parse("package:${requireActivity().packageName}")
            startActivityForResult(myIntent, APP_PERMISSIONS)
        }
    }
}