package com.example.myapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.confirmit.mobilesdk.database.externals.Program
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentTriggerOverviewBinding
import com.example.myapp.fragments.viewmodels.TriggerOverviewViewModel
import com.example.myapp.fragments.viewmodels.models.ProgramModel
import com.example.myapp.utils.Utils
import kotlinx.coroutines.launch

enum class ProgramConfigType {
    ON_EVENT_TRIGGER,
    CUSTOM_DATA,
    REMOVE_PROGRAM
}

interface OnTriggerOverviewItemClicked {
    fun onConfigClicked(config: ProgramConfigType)
}


class TriggerOverviewFragment(private val program: Program) : Fragment(), OnTriggerOverviewItemClicked, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance(program: Program): TriggerOverviewFragment {
            return TriggerOverviewFragment(program)
        }
    }

    private lateinit var overviewAdapter: TriggerOverviewAdapter
    private lateinit var viewModel: TriggerOverviewViewModel

    private var _binding: FragmentTriggerOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTriggerOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TriggerOverviewViewModel::class.java)
        viewModel.setProgramModel(program)

        overviewAdapter = TriggerOverviewAdapter((activity as MainActivity), viewModel, this)
        binding.triggerOverviewTable.adapter = overviewAdapter
        binding.triggerOverviewTable.layoutManager = LinearLayoutManager(context)

        binding.triggerOverviewSwipeRefresh.setOnRefreshListener(this)
        binding.triggerOverviewSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        binding.mainToolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).navigateToList()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.viewModelScope.launch {
            viewModel.downloadCounters()
        }
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
                input.isSingleLine = false
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
                    }
                    .setOnDismissListener {
                        viewModel.program.configs.customData = input.text.toString()
                        overviewAdapter.notifyDataSetChanged()
                    }
                    .show()
            }
            ProgramConfigType.REMOVE_PROGRAM -> {
                if (viewModel.removeProgram()) {
                    (activity as? MainActivity)?.navigateToList()
                }
            }
        }
    }

    override fun onRefresh() {
        viewModel.viewModelScope.launch {
            viewModel.downloadProgram()
            binding.triggerOverviewSwipeRefresh.isRefreshing = false
        }
    }

    abstract class TriggerOverviewItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setup(program: ProgramModel, config: ProgramConfigType?)
    }

    class TriggerOverviewCardViewHolder(view: View) : TriggerOverviewItemViewHolder(view) {
        private val txtName: TextView = view.findViewById(R.id.triggerOverviewCardName)

        override fun setup(program: ProgramModel, config: ProgramConfigType?) {
            txtName.text = program.program.programKey
        }
    }

    class TriggerOverviewDoubleConfigViewHolder(view: View, private val triggerOverviewClicked: OnTriggerOverviewItemClicked) : TriggerOverviewItemViewHolder(view),
        View.OnClickListener {
        private lateinit var configType: ProgramConfigType
        private val title: TextView = view.findViewById(R.id.txtTitle)
        private val secondary: TextView = view.findViewById(R.id.txtSubtitle)
        private val imgButton: AppCompatImageButton = view.findViewById(R.id.btnImage)

        init {
            view.isClickable = true
            view.setOnClickListener(this)
        }

        override fun setup(program: ProgramModel, config: ProgramConfigType?) {
            this.configType = config!!

            when (config) {
                ProgramConfigType.CUSTOM_DATA -> {
                    title.text = "Custom Data Value (eg. <key>=<value>;)"
                    secondary.text = if (program.configs.customData.isEmpty()) "(Not Set)" else program.configs.customData
                    imgButton.setImageResource(R.drawable.baseline_vpn_key_black_24)
                }
                else -> {
                }
            }
        }

        override fun onClick(p0: View?) {
            triggerOverviewClicked.onConfigClicked(configType)
        }
    }

    class TriggerOverviewSingleConfigViewHolder(
        private val mainActivity: MainActivity,
        private val view: View,
        private val triggerOverviewItemClicked: OnTriggerOverviewItemClicked
    ) : TriggerOverviewItemViewHolder(view), View.OnClickListener {
        private lateinit var configType: ProgramConfigType
        private val title: TextView = view.findViewById(R.id.txtLabel)
        private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

        init {
            view.isClickable = true
            view.setOnClickListener(this)
        }

        override fun setup(program: ProgramModel, config: ProgramConfigType?) {
            this.configType = config!!

            when (configType) {
                ProgramConfigType.ON_EVENT_TRIGGER -> {
                    title.text = "Trigger onTestEvent"
                    imgIcon.setImageResource(R.drawable.ic_assignment_black)
                    view.contentDescription = "trigger_event"
                }
                ProgramConfigType.REMOVE_PROGRAM -> {
                    title.text = "Remove Program"
                    title.setTextColor(ContextCompat.getColor(title.context, R.color.error))
                    imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, R.color.icon))
                    imgIcon.setImageResource(R.drawable.baseline_delete_black_24)
                }
                else -> {
                }
            }
        }

        override fun onClick(p0: View?) {
            triggerOverviewItemClicked.onConfigClicked(configType)
        }
    }

    enum class TriggerOverviewViewType(val value: Int) {
        OVERVIEW(0),
        SINGLE(1),
        DOUBLE(2);

        companion object {
            fun fromValue(value: Int): TriggerOverviewViewType? = TriggerOverviewViewType.values().find { it.value == value }
        }
    }

    class TriggerOverviewAdapter(
        private val mainActivity: MainActivity,
        private val viewModel: TriggerOverviewViewModel,
        private val itemClickedListener: OnTriggerOverviewItemClicked
    ) : RecyclerView.Adapter<TriggerOverviewItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerOverviewItemViewHolder {
            val type = TriggerOverviewViewType.fromValue(viewType)!!
            return when (type) {
                TriggerOverviewViewType.OVERVIEW -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.app_overview_card_trigger, parent, false)
                    TriggerOverviewCardViewHolder(view)
                }
                TriggerOverviewViewType.SINGLE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_single_line, parent, false)
                    TriggerOverviewSingleConfigViewHolder(mainActivity, view, itemClickedListener)
                }
                TriggerOverviewViewType.DOUBLE -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_two_line, parent, false)
                    TriggerOverviewDoubleConfigViewHolder(view, itemClickedListener)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> {
                    TriggerOverviewViewType.OVERVIEW.value
                }
                else -> {
                    when (ProgramConfigType.values()[position - 1]) {
                        ProgramConfigType.REMOVE_PROGRAM, ProgramConfigType.ON_EVENT_TRIGGER -> {
                            TriggerOverviewViewType.SINGLE.value
                        }
                        else -> {
                            TriggerOverviewViewType.DOUBLE.value
                        }
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: TriggerOverviewItemViewHolder, position: Int) {
            when (position) {
                0 -> {
                    holder.setup(viewModel.program, null)
                }
                else -> {
                    val config = ProgramConfigType.values()[position - 1]
                    holder.setup(viewModel.program, config)
                }
            }
        }

        override fun getItemCount(): Int {
            return ProgramConfigType.values().size + 1
        }
    }
}