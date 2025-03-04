package tik.prometheus.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZPagingDataAdapter
import tik.prometheus.mobile.databinding.ItemPagingGreenhouseBinding
import tik.prometheus.mobile.databinding.ItemPagingSeperatorBinding
import tik.prometheus.mobile.ui.screen.greenhouse.GreenhouseModel

class GreenhouseAdapter(var parent: NestableFragment<GreenhouseModel.GreenhouseItem>) : ZPagingDataAdapter<GreenhouseModel, RecyclerView.ViewHolder>(GreenhouseComparator) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val greenhouseModel = getItem(position)
        greenhouseModel.let {
            when (greenhouseModel) {
                is GreenhouseModel.GreenhouseItem -> {
                    val viewHolder = holder as GreenhouseViewHolder
                    val greenhouse = greenhouseModel.greenhouse
                    viewHolder.ghItemBinding.txtGreenhouseId.text = greenhouse.id.toString()
                    viewHolder.ghItemBinding.txtGreenhouseType.text = greenhouse.type?.value
                    viewHolder.ghItemBinding.txtGreenhouseLabel.text = greenhouse.label

                    holder.itemView.setOnClickListener(View.OnClickListener {
                        parent.insertNestedFragment(greenhouseModel)
                    })
                }

                is GreenhouseModel.SeparatorItem -> {
                    val viewHolder = holder as SeparatorViewHolder
                    viewHolder.itemSeperatorBinding.separatorDescription.text = greenhouseModel.description
                }

                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GreenhouseModel.GreenhouseItem -> R.layout.item_paging_greenhouse
            is GreenhouseModel.SeparatorItem -> R.layout.item_paging_seperator
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_paging_greenhouse -> GreenhouseViewHolder(ItemPagingGreenhouseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> SeparatorViewHolder(ItemPagingSeperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    class GreenhouseViewHolder(val ghItemBinding: ItemPagingGreenhouseBinding) : RecyclerView.ViewHolder(ghItemBinding.root)

    object GreenhouseComparator : DiffUtil.ItemCallback<GreenhouseModel>() {
        override fun areItemsTheSame(oldItem: GreenhouseModel, newItem: GreenhouseModel): Boolean {
            return (oldItem is GreenhouseModel.GreenhouseItem && newItem is GreenhouseModel.GreenhouseItem && oldItem.greenhouse.id == newItem.greenhouse.id)
        }

        override fun areContentsTheSame(oldItem: GreenhouseModel, newItem: GreenhouseModel): Boolean =
            oldItem == newItem

    }

}