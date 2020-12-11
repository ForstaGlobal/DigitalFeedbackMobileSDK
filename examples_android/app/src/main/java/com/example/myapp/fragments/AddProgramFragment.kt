package com.example.myapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.confirmit.mobilesdk.ConfirmitServer
import com.example.myapp.R
import com.example.myapp.databinding.FragmentAddProgramBinding
import com.example.myapp.utils.hideKeyboard

interface OnProgramAddedListener {
    fun onProgramAdded(serverId: String, programKey: String)
}

class AddProgramFragment : DialogFragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val serverNameList: List<String> = ConfirmitServer.getServers().map { x -> x.name }
    private var selectedServerPos: Int = 0

    var listener: OnProgramAddedListener? = null

    private var _binding: FragmentAddProgramBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.addProgramDone.setOnClickListener(this)

        val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, serverNameList)
        binding.addProgramHost.adapter = adapter
        binding.addProgramHost.onItemSelectedListener = this
        binding.addProgramHost.setSelection(selectedServerPos)

        binding.mainToolbar.setNavigationOnClickListener { dismiss() }
    }

    override fun onClick(p0: View?) {
        hideKeyboard()
        val programKey = binding.addProgramKey.text.toString()
        val serverId = ConfirmitServer.getServers()[selectedServerPos].serverId

        listener?.onProgramAdded(serverId, programKey)
        dismiss()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedServerPos = position
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}