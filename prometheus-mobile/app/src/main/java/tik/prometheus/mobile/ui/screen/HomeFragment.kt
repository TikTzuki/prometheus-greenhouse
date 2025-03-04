package tik.prometheus.mobile.ui.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.github.mikephil.charting.data.LineData
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentHomeBinding
import tik.prometheus.mobile.ui.adapters.SensorAdapter
import tik.prometheus.mobile.ui.screen.sensor.SensorModel
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.mobile.utils.themeColor
import tik.prometheus.mobile.utils.underline
import tik.prometheus.rest.constants.NullableSensorTypeModel
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.Greenhouse
import java.time.LocalDate
import java.util.*

class HomeFragment : ZFragment(), NestableFragment<SensorModel.SensorItem> {
    val TAG = HomeFragment::class.toString()

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory(zContainer.sensorRepository) }
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val sensorAdapter = SensorAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setupChart()
        setupSearcher()
        setupSensorList()


        return binding.root
    }

    private fun setupChart() {
        // CHAR
        viewModel.sensorLineChartData.observe(requireActivity()) {
            val data = LineData(it)
            binding.lineChart.data = data
            binding.lineChart.invalidate()
            val color: Int = requireContext().themeColor(R.attr.colorOnBackground)
            binding.lineChart.axisLeft.textColor = color
            binding.lineChart.axisRight.textColor = color
            binding.lineChart.legend.textColor = color
            binding.lineChart.lineData.setValueTextColor(color)
            binding.lineChart.description.textColor = color
            binding.lineChart.description.text = ""
        }
        // Date range

        binding.txtFromValue.text = underline(viewModel.fromDate.value.toString())
        binding.txtFromValue.inputType = InputType.TYPE_NULL
        viewModel.fromDate.observe(requireActivity()) {
            binding.txtFromValue.text = underline(it.toString())
        }
        binding.txtFromValue.setOnClickListener {
            val pickedValue = viewModel.fromDate.value
            val picker = DatePickerDialog(
                requireContext(), { context, year, month, dayOfMonth ->
                    viewModel.postFromDate(LocalDate.of(year, month + 1, dayOfMonth))
                },
                pickedValue!!.year, pickedValue.monthValue - 1, pickedValue.dayOfMonth
            )
            picker.show()
        }

        binding.txtToValue.text = underline(viewModel.toDate.value.toString())
        binding.txtToValue.inputType = InputType.TYPE_NULL
        viewModel.toDate.observe(requireActivity()) {
            binding.txtToValue.text = underline(it.toString())
        }
        binding.txtToValue.setOnClickListener {
            val pickedValue = viewModel.toDate.value
            val picker = DatePickerDialog(
                requireContext(), { context, year, month, dayOfMonth ->
                    viewModel.postToDate(LocalDate.of(year, month + 1, dayOfMonth))
                },
                pickedValue!!.year, pickedValue.monthValue - 1, pickedValue.dayOfMonth
            )
            picker.show()
        }

        // Select sensor
        viewModel.selectSensors.observe(requireActivity()) {
            binding.txtSelectSensorCount.text = it?.size.toString()
            viewModel.loadSensorLineChartData(
                arrayListOf<Int>(
                    requireContext().themeColor(R.color.cyan_200),
                    requireContext().themeColor(R.color.cyan_500),
                    requireContext().themeColor(R.color.cyan_700),
                    requireContext().themeColor(R.color.cyan_900),
                )
            )
        }
        binding.clSelectedSensorCount.setOnClickListener(View.OnClickListener {
            val dialog = SimpleSearchDialogCompat(context, "Search", "Find sensor type", null, viewModel.selectSensors.value?.values?.toCollection(ArrayList()),
                SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                    when (searchable) {
                        is SensorModel.SensorItem -> {
                            viewModel.selectSensors.value?.remove(searchable.sensor.id)
                            viewModel.selectSensors.postValue(viewModel.selectSensors.value)
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

    private fun setupSearcher() {
        viewModel.loadGreenhouse()

        viewModel.selectedSensorType.observe(requireActivity()) {
            var sensorText = it?.sensorType?.name ?: resources.getText(R.string.sensor_type).toString()
            binding.sensorLayout.txtSearchSensorType.text = underline(sensorText)
            viewModel.loadSensors()
        }
        binding.sensorLayout.txtSearchSensorType.setOnClickListener {
            editType()
        }
        viewModel.selectedGreenhouse.observe(requireActivity()) {
            var greenhouseLabel: String = it?.label ?: resources.getText(R.string.greenhouse).toString()
            binding.sensorLayout.txtSearchGreenhouse.text = underline(greenhouseLabel)
            viewModel.loadSensors()
        }
        binding.sensorLayout.txtSearchGreenhouse.setOnClickListener {
            selectGreenhouse()
        }
    }

    private fun setupSensorList() {
        viewModel.sensors.observe(requireActivity()) {
            it?.let {
                sensorAdapter.initAdapter(binding.sensorLayout.sensorsRecyclerView, binding.sensorLayout.btnRetry, it, lifecycleScope)
                sensorAdapter.addLoadStateListener(binding.sensorLayout.btnRetry, binding.sensorLayout.progressBar) { it -> showToast(it.error.toString()) }
                sensorAdapter.onLongClickListenerFactory = OnLongClickSensorItemListenerFactory()
            }
        }
    }

    inner class OnLongClickSensorItemListenerFactory : SensorAdapter.OnLongClickListenerFactory {
        @SuppressLint("ClickableViewAccessibility")
        override fun create(viewHolder: SensorAdapter.SensorViewHolder, sensorModel: SensorModel.SensorItem): View.OnLongClickListener {
            return View.OnLongClickListener { v ->
                if (viewModel.selectSensors.value?.containsKey(sensorModel.sensor.id) == false) {
                    viewModel.selectSensors.value?.put(sensorModel.sensor.id, sensorModel)
                    viewModel.selectSensors.postValue(viewModel.selectSensors.value)
                } else {
                    viewModel.selectSensors.value?.remove(sensorModel.sensor.id)
                    viewModel.selectSensors.postValue(viewModel.selectSensors.value)
                }

                true
            }
        }

    }


    override fun insertNestedFragment(model: SensorModel.SensorItem) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container)
        val pair = Pair(Utils.KEY_SENSOR_ID, model.sensor.id)
        val args = bundleOf(pair)
        navController.navigate(R.id.nav_sensor_detail, args)
    }

    private fun editType() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find sensor type", null, SensorType.getNullableModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is NullableSensorTypeModel -> {
                        viewModel.postSelectedSensorType(searchable)
                    }

                    else -> {
                    }
                }
                baseSearchDialogCompat.dismiss()
            }
        )
        dialog.show()
    }
    private fun selectGreenhouse(){
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find greenhouse", null, viewModel.greenhouses,
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is Greenhouse -> {
                        viewModel.postSelectedGreenhouse(searchable)
                    }

                    else -> {
                    }
                }
                baseSearchDialogCompat.dismiss()
            }
        )
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}