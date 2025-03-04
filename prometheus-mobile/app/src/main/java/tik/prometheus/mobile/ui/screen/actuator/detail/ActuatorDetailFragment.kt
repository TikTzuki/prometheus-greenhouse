package tik.prometheus.mobile.ui.screen.actuator.detail

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import org.eclipse.paho.android.service.MqttAndroidClient
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentActuatorDetailBinding
import tik.prometheus.mobile.models.ActuatorTask
import tik.prometheus.mobile.services.MqttHelper
import tik.prometheus.mobile.services.MqttSensorViewHolder
import tik.prometheus.mobile.ui.screen.DoubleTapListener
import tik.prometheus.mobile.ui.screen.sensor.SensorModel
import tik.prometheus.mobile.utils.*
import tik.prometheus.rest.constants.ActuatorType
import tik.prometheus.rest.constants.ActuatorTypeModel
import tik.prometheus.rest.constants.SensorType


class ActuatorDetailFragment : ZFragment(), MqttSensorViewHolder {
    val TAG = ActuatorDetailFragment::class.java.toString()
    private var _binding: FragmentActuatorDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActuatorDetailViewModel by viewModels { ActuatorDetailViewModel.Factory(zContainer.actuatorRepository) }
    private var mqttAndroidClient: MqttAndroidClient? = null
    private var actuatorStateClient: MqttAndroidClient? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActuatorDetailBinding.inflate(inflater, container, false)

        initActuatorInfo()

        setUpRunOn()

        setUpTaskOnSensor()

        setUpIsRunningButton()

        binding.btnSave.setOnClickListener(View.OnClickListener { viewModel.saveActuator() })
        return binding.root
    }

    private fun initActuatorInfo() {
        val actuatorId = requireArguments().getLong(Utils.KEY_ACTUATOR_ID)
        viewModel.postActuatorId(actuatorId)

        viewModel.actuatorId.observe(requireActivity()) {
            viewModel.fetchAll()
        }

        viewModel.actuator.observe(requireActivity(), Observer {
            binding.txtId.text = it.id.toString()
            binding.txtLocalId.text = it.localId
            binding.txtTypeId.text = it.type.value
            binding.txtLabel.text = it.label
            binding.txtNorth.text = it.north.toString()
            binding.txtWest.text = it.west.toString()
            Log.d(TAG, "initActuatorInfoObserve: " + it)
            actuatorStateClient = MqttHelper.createSensorListener(binding.root.context, it.topic, this)
        })
        binding.llType.setOnClickListener(DoubleTapListener(requireContext()) {
            editType()
        })
        binding.llLabel.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.actuator.value?.let {
                editText(it.label, "Label") { dialog, input ->
                    binding.txtLabel.text = input.text
                    it.label = input.text.toString()
                    dialog.dismiss()
                }
            }
        })
        binding.llNorth.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.actuator.value?.let {
                editText(it.north.toString(), "North", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtNorth.text = input.text
                    it.north = input.text.toString().toFloatOrNull()
                    dialog.dismiss()
                }
            }
        })
        binding.llWest.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.actuator.value?.let {
                editText(it.west.toString(), "West", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtWest.text = input.text
                    it.west = input.text.toString().toFloatOrNull()
                    dialog.dismiss()
                }
            }
        })

        viewModel.loadState.observe(requireActivity(), Observer {
            when (it) {
                is ZLoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE
                }

                is ZLoadState.Error -> {
                    binding.btnRetry.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE
                }

                is ZLoadState.NotLoading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.GONE
                }
            }
        })

        Log.d(TAG, "after initActuatorInfo: " + viewModel.actuatorId.value)
    }

    fun setUpRunOn() {

        binding.txtRunOn.setOnClickListener(View.OnClickListener {
            val dialog = SimpleSearchDialogCompat(context, "Search", "Run on", null, RunOn.values().map { RunOnModel.RunOnItem(it) }.toCollection(ArrayList()),
                SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: RunOnModel.RunOnItem, i: Int ->
                    when (searchable.value) {
                        RunOn.DECISION_TREE -> {
                            viewModel.postActuatorTask(ActuatorTask(taskType = ActuatorTaskType.DECISION, startValue = 0F, limitValue = 0F))
                            binding.lnSensorTask.visibility = View.VISIBLE
                        }

                        RunOn.SENSOR_VALUE -> {
                            viewModel.postActuatorTask(ActuatorTask(taskType = ActuatorTaskType.RANGE, startValue = 0F, limitValue = 0F))
                            binding.lnSensorTask.visibility = View.VISIBLE
                        }

                        RunOn.NaN -> {
                            viewModel.postActuatorTask(null)
                            binding.lnSensorTask.visibility = View.GONE
                        }
                    }
                    baseSearchDialogCompat.dismiss()
                }
            )
            dialog.show()
        })
//        viewModel.runOn.observe(requireActivity()) {
//            Log.d(TAG, "setUpRunOnObserve: $it")
//            binding.txtRunOn.text = underline(it.name)
//            when (it) {
//                RunOn.SENSOR_VALUE -> {
//                    binding.lnSensorTask.visibility = View.VISIBLE
//                }
//
//                RunOn.DECISION_TREE -> {
//                    binding.lnSensorTask.visibility = View.VISIBLE
//                }
//
//                RunOn.NaN -> {
//                    binding.lnSensorTask.visibility = View.GONE
//                }
//
//                else -> {
//
//                }
//            }
//            viewModel.postActuatorTask(null)
//        }


    }

    fun setUpTaskOnSensor() {
        viewModel.actuatorTask.observe(requireActivity()) {
            Log.d(TAG, "setUpTaskOnSensorObserve: ")
            loadActuatorTask()
        }

        binding.txtSensorId.setOnClickListener(DoubleTapListener(requireContext()) {
            val dialog = SimpleSearchDialogCompat(context, "Search", "Find sensor", null,
                viewModel.sensors.value?.map { SensorModel.SensorItem(it) }?.toCollection(ArrayList()),
                SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                    when (searchable) {
                        is SensorModel.SensorItem -> {
                            val newTask = ActuatorTask(
                                searchable.sensor.id,
                                ActuatorTaskType.RANGE,
                                searchable.sensor.topic,
                                searchable.sensor.type ?: SensorType.NaN,
                                searchable.sensor.unit ?: EUnit.NaN,
                                searchable.sensor.label,
                                0F,
                                0F
                            )
                            viewModel.postActuatorTask(newTask)
                            Log.d(TAG, "setUpTaskOnSensor: " + viewModel.actuatorTask.value)
                        }

                        else -> {
                        }
                    }
                    baseSearchDialogCompat.dismiss()
                }
            )
            dialog.show()
        })
    }

    private fun loadActuatorTask() {
        binding.txtRunOn.text = underline(RunOn.NaN.name)
        binding.lnSensorTask.visibility = View.GONE
//        binding.txtSensorId.text = underline("NaN")
//        binding.txtSensorLabel.text = ""
//        binding.txtSensorType.text = "NaN"
//        binding.txtSensorUnit.text = "NaN"
//        binding.txtSensorTargetValueFrom.text = "0"
//        binding.txtSensorTargetValueTo.text = "0"

        viewModel.actuatorTask.value?.let {
            Log.d(TAG, "loadActuatorTask: $it")
            when (it.taskType) {
                ActuatorTaskType.DECISION -> {
                    binding.txtRunOn.text = underline(RunOn.DECISION_TREE.name)
                    binding.lnSensorTask.visibility = View.VISIBLE
                }

                ActuatorTaskType.RANGE -> {
                    binding.txtRunOn.text = underline(RunOn.SENSOR_VALUE.name)
                    binding.lnSensorTask.visibility = View.VISIBLE
                }

                else -> {}
            }
            mqttAndroidClient = MqttHelper.createSensorListener(binding.root.context, it.sensorTopic, this)
            binding.txtSensorId.text = underline(it.sensorId.toString())
            binding.txtSensorLabel.text = it.sensorLabel
            binding.txtSensorType.text = it.sensorType.value
            binding.txtSensorUnit.text = it.sensorUnit.value
            binding.txtSensorTargetValueFrom.text = it.startValue.toString()
            binding.txtSensorTargetValueTo.text = it.limitValue.toString()

            binding.txtSensorTargetValueFrom.setOnClickListener(DoubleTapListener(requireContext()) {
                viewModel.actuatorTask.value?.let {
                    editText(it.startValue.toString(), "From", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                        binding.txtSensorTargetValueFrom.text = input.text
                        it.startValue = input.text.toString().toFloatOrNull() ?: 0f
                        dialog.dismiss()
                    }
                }
            })
            binding.txtSensorTargetValueTo.setOnClickListener(DoubleTapListener(requireContext()) {
                viewModel.actuatorTask.value?.let {
                    editText(it.limitValue.toString(), "To", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                        binding.txtSensorTargetValueTo.text = input.text
                        it.limitValue = input.text.toString().toFloatOrNull() ?: 0f
                        dialog.dismiss()
                    }
                }
            })

        }
    }

    fun setUpIsRunningButton() {
        viewModel.isRunning.observe(requireActivity()) {
            println("is running: $it")
            try {
                if (it) {
                    binding.ivActuatorBg.setImageResource(R.drawable.pink_neon);
                    binding.txtActuatorState.text = resources.getString(R.string.off)
                } else {
                    binding.ivActuatorBg.setImageResource(R.drawable.blue_neon);
                    binding.txtActuatorState.text = resources.getString(R.string.on)
                }
            } catch (e: Exception) {
            }
        }

        binding.ivActuatorBg.setOnClickListener {
            viewModel.toggleRunning()
        };
    }

    private fun editType() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find actuator type", null, ActuatorType.getModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is ActuatorTypeModel -> {
                        binding.txtTypeId.text = searchable.actuatorType.value
                        viewModel.actuator.value?.type = searchable.actuatorType
                        baseSearchDialogCompat.dismiss()
                    }

                    else -> {
                    }
                }
            }
        )
        dialog.show()
    }

//    fun editText(defaultValue: String?, title: String, inputType: Int = InputType.TYPE_CLASS_TEXT, onOk: (dialog: DialogInterface, input: EditText) -> Unit) {
//        val input = EditText(requireContext())
//        input.inputType = inputType
//        input.setText(defaultValue)
//        val alert = AlertDialog.Builder(requireContext())
//            .setTitle(title)
//            .setView(input)
//            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
//                onOk.invoke(dialog, input)
//            })
//            .create()
//        alert.show()
//    }

    override fun updateMqttValue(topic: String?, value: String?) {
        when (topic) {
            viewModel.actuatorTask.value?.sensorTopic -> {
                binding.txtSensorValue.text = value
            }

            viewModel.actuator.value?.topic -> {
                viewModel.postRunning(value == "1")
            }
        }

    }
}