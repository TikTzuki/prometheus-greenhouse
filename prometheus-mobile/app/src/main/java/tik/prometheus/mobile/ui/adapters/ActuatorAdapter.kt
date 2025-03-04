package tik.prometheus.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZPagingDataAdapter
import tik.prometheus.mobile.databinding.ItemPagingActuatorBinding
import tik.prometheus.mobile.databinding.ItemPagingSeperatorBinding
import tik.prometheus.mobile.ui.screen.actuator.ActuatorModel

class ActuatorAdapter(var parent: NestableFragment<ActuatorModel.ActuatorItem>) : ZPagingDataAdapter<ActuatorModel, RecyclerView.ViewHolder>(ActuatorComparator) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        model.let {
            when (model) {
                is ActuatorModel.ActuatorItem -> {
                    val viewHolder = holder as ActuatorViewHolder
                    val actuator = model.actuator
                    viewHolder.itemBinding.txtActuatorIdValue.text = actuator.id.toString()
                    viewHolder.itemBinding.txtActuatorTitleValue.text = actuator.label
                    viewHolder.itemBinding.txtActuatorTypeValue.text = actuator.type.value
                    holder.itemView.setOnClickListener {
                        parent.insertNestedFragment(model)
                    }
                }

                is ActuatorModel.SeparatorItem -> {
                    val viewHolder = holder as SeparatorViewHolder
                    viewHolder.itemSeperatorBinding.separatorDescription.text = model.description
                }

                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ActuatorModel.ActuatorItem -> R.layout.item_paging_actuator
            is ActuatorModel.SeparatorItem -> R.layout.item_paging_seperator
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_paging_actuator -> ActuatorViewHolder(ItemPagingActuatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> SeparatorViewHolder(ItemPagingSeperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    class ActuatorViewHolder(val itemBinding: ItemPagingActuatorBinding) : RecyclerView.ViewHolder(itemBinding.root)

    object ActuatorComparator : DiffUtil.ItemCallback<ActuatorModel>() {
        override fun areItemsTheSame(oldItem: ActuatorModel, newItem: ActuatorModel): Boolean {
            return (oldItem is ActuatorModel.ActuatorItem && newItem is ActuatorModel.ActuatorItem && oldItem.actuator.id == newItem.actuator.id)
        }

        override fun areContentsTheSame(oldItem: ActuatorModel, newItem: ActuatorModel): Boolean =
            oldItem == newItem

    }

}