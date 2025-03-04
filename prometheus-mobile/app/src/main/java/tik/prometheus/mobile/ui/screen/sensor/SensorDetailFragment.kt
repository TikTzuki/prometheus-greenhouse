package tik.prometheus.mobile.ui.screen.sensor

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import org.eclipse.paho.android.service.MqttAndroidClient
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentSensorDetailBinding
import tik.prometheus.mobile.services.MqttHelper
import tik.prometheus.mobile.services.MqttSensorViewHolder
import tik.prometheus.mobile.ui.screen.DoubleTapListener
import tik.prometheus.mobile.utils.EUnit
import tik.prometheus.mobile.utils.UnitModel
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.mobile.utils.ZLoadState
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.constants.SensorTypeModel

class SensorDetailFragment : ZFragment(), MqttSensorViewHolder {
    private var _binding: FragmentSensorDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SensorDetailViewModel by viewModels { SensorDetailViewModel.Factory(zContainer.sensorRepository) }
    private var mqttAndroidClient: MqttAndroidClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSensorDetailBinding.inflate(inflater, container, false)

        viewModel.sensorId.postValue(requireArguments().getLong(Utils.KEY_SENSOR_ID))
        viewModel.sensorId.observe(requireActivity()) {
            viewModel.loadSensor()
        }

        viewModel.sensor.observe(requireActivity(), Observer {
            mqttAndroidClient = MqttHelper.createSensorListener(binding.root.context, it.topic, this)
            binding.txtSensorUnit.text = it.unit?.annotate
            binding.txtGreenhouseValue.text = it.greenhouse.label
            binding.txtIdValue.text = it.id.toString()
            binding.txtLocalIdValue.text = it.localId
            binding.txtTypeValue.text = it.type?.value
            binding.txtUnitValue.text = it.unit?.toString()
            binding.txtLabelValue.text = it.label
        })
        binding.llUnit.setOnClickListener(DoubleTapListener(requireContext()) {
            editUnit()
        })
        binding.llType.setOnClickListener(DoubleTapListener(requireContext()) {
            editType()
        })
        binding.llLabel.setOnClickListener(DoubleTapListener(requireContext()) {
            editLabel()
        })
        binding.btnSave.setOnClickListener(View.OnClickListener { viewModel.saveSensor() })

        viewModel.loadState.observe(requireActivity(), Observer {
            when (it) {
                is ZLoadState.Error -> {
                    showToast(it.error.toString())
                }

                else -> {}
            }
        })
        return binding.root
    }

    override fun updateMqttValue(topic: String, value: String?) {
        binding.txtSensorValue.text = value
    }

    private fun editUnit() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find unit", null, EUnit.getModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is UnitModel.UnitItem -> {
                        binding.txtUnitValue.text = searchable.unit.value
                        viewModel.sensor.value?.unit = searchable.unit
                        baseSearchDialogCompat.dismiss()
                    }

                    else -> {
                    }
                }
            }
        )
        dialog.show()
    }

    private fun editType() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find sensor type", null, SensorType.getModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is SensorTypeModel -> {
                        binding.txtTypeValue.text = searchable.sensorType.value
                        viewModel.sensor.value?.type = searchable.sensorType
                        baseSearchDialogCompat.dismiss()
                    }

                    else -> {
                    }
                }
            }
        )
        dialog.show()
    }

    private fun editLabel() {
        val input = EditText(requireContext())
        input.setText(viewModel.sensor.value?.label)
        val alert = AlertDialog.Builder(requireContext())
            .setTitle("Label")
            .setView(input)
            .setPositiveButton(resources.getText(R.string.ok), DialogInterface.OnClickListener { dialogInterface, which ->
                binding.txtLabelValue.text = input.text
                viewModel.sensor.value?.label = input.text.toString()
                dialogInterface.dismiss()
            })
            .create()

        alert.show()
    }

}