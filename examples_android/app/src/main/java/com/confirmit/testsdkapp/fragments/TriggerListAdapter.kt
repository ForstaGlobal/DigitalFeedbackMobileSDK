package com.confirmit.testsdkapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.mobilesdk.ConfirmitServer
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.viewmodels.TriggerListViewModel
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel
import com.google.android.material.card.MaterialCardView

interface OnTriggerCardViewClicked {
    fun onCardClicked(program: ProgramModel)
}

class TriggerListViewHolder(view: View, private val cardClickListener: OnTriggerCardViewClicked) : RecyclerView.ViewHolder(view), View.OnClickListener {
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

class TriggerListAdapter(private val viewModel: TriggerListViewModel, private val cardClickListner: OnTriggerCardViewClicked) : RecyclerView.Adapter<TriggerListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_card_trigger, parent, false)
        return TriggerListViewHolder(view, cardClickListner)
    }

    override fun getItemCount(): Int {
        return viewModel.programs.count()
    }

    override fun onBindViewHolder(holder: TriggerListViewHolder, position: Int) {
        holder.setup(viewModel.programs[position])
    }
}