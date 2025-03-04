package tik.prometheus.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZPagingDataAdapter
import tik.prometheus.mobile.databinding.ItemPagingSensorBinding
import tik.prometheus.mobile.databinding.ItemPagingSeperatorBinding
import tik.prometheus.mobile.services.MqttHelper
import tik.prometheus.mobile.services.MqttSensorViewHolder
import tik.prometheus.mobile.ui.screen.sensor.SensorModel

class SensorAdapter(var parent: NestableFragment<SensorModel.SensorItem>) : ZPagingDataAdapter<SensorModel, RecyclerView.ViewHolder>(SensorComparator) {
    var onLongClickListenerFactory: OnLongClickListenerFactory? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensorModel = getItem(position)
        sensorModel.let {
            when (sensorModel) {
                is SensorModel.SensorItem -> {
                    val viewHolder = holder as SensorViewHolder
                    sensorModel.mqttClient = MqttHelper.createSensorListener(viewHolder.sensorItemBinding.root.context, sensorModel.sensor.topic, viewHolder);
                    viewHolder.sensorItemBinding.txtId.text = "%s-%s".format(sensorModel.sensor.id, sensorModel.sensor.label.toString())
                    viewHolder.sensorItemBinding.txtSensorType.text = sensorModel.sensor.type?.value
                    viewHolder.sensorItemBinding.value.text = "0"
                    viewHolder.sensorItemBinding.unit.text = sensorModel.sensor.unit?.annotate

                    holder.itemView.setOnClickListener(View.OnClickListener {
                        parent.insertNestedFragment(sensorModel)
                    })
                    holder.itemView.setOnLongClickListener(onLongClickListenerFactory?.create(viewHolder, sensorModel))
                }

                is SensorModel.SeparatorItem -> {
                    val viewHolder = holder as SeparatorViewHolder
                    viewHolder.itemSeperatorBinding.separatorDescription.text = sensorModel.description
                }

                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SensorModel.SensorItem -> R.layout.item_paging_sensor
            is SensorModel.SeparatorItem -> R.layout.item_paging_seperator
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_paging_sensor -> SensorViewHolder(
                ItemPagingSensorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> SeparatorViewHolder(
                ItemPagingSeperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class SensorViewHolder(val sensorItemBinding: ItemPagingSensorBinding) : RecyclerView.ViewHolder(sensorItemBinding.root), MqttSensorViewHolder {
        override fun updateMqttValue(topic: String, value: String) {
            sensorItemBinding.value.text = value
        }

    }

    interface OnLongClickListenerFactory {
        fun create(viewHolder: SensorViewHolder, sensorModel: SensorModel.SensorItem): View.OnLongClickListener
    }

    object SensorComparator : DiffUtil.ItemCallback<SensorModel>() {
        override fun areItemsTheSame(oldItem: SensorModel, newItem: SensorModel): Boolean {
            return (oldItem is SensorModel.SensorItem && newItem is SensorModel.SensorItem &&
                    oldItem.sensor.id == newItem.sensor.id)
        }

        override fun areContentsTheSame(oldItem: SensorModel, newItem: SensorModel): Boolean =
            oldItem == newItem

    }


}