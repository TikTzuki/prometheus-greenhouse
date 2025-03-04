package tik.prometheus.mobile.ui.screen.greenhouse

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.paging.LoadState
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentGreenhouseDetailBinding
import tik.prometheus.mobile.ui.screen.DoubleTapListener
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.mobile.utils.underline
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.constants.GreenhouseTypeModel

class GreenhouseDetailFragment : ZFragment() {
    val TAG = GreenhouseDetailFragment::class.toString()
    val viewModel: GreenhouseDetailViewModel by viewModels { GreenhouseDetailViewModel.Factory(zContainer.greenhouseRepository) }
    private var _binding: FragmentGreenhouseDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGreenhouseDetailBinding.inflate(inflater, container, false)

        viewModel.greenhouseId.postValue(requireArguments().getLong(Utils.KEY_GREENHOUSE_ID))
        viewModel.greenhouseId.observe(requireActivity()) {
            viewModel.getGreenhouseDetail()
        }

        viewModel.loadState.observe(requireActivity(), Observer {
            when (it) {
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE
                }

                is LoadState.Error -> {
                    binding.btnRetry.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.GONE
                }
            }
        })

        setUpGreenhouseDetail()

        binding.btnActuatorCount.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
            val args = bundleOf(Utils.KEY_GREENHOUSE_ID to viewModel.greenhouse.value?.id)
            navController.navigate(R.id.nav_actuator, args);
        }

        binding.btnSensorCount.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
            val args = bundleOf(Utils.KEY_GREENHOUSE_ID to viewModel.greenhouse.value?.id)
            navController.navigate(R.id.nav_sensor, args);
        }

        binding.btnSave.setOnClickListener{
            viewModel.saveGreenhouse()
        }

        return binding.root;
    }

    private fun setUpGreenhouseDetail() {
        viewModel.greenhouse.observe(requireActivity(), Observer {
            println("observer load gh")
            binding.txtId.text = it.id.toString()
            binding.txtType.text = underline(it.type?.value ?: GreenhouseType.NaN.value)
            binding.txtLabel.text = underline(it.label ?: "")
            binding.txtArea.text = underline(it.area.toString())
            binding.txtCultivationArea.text = underline(it.cultivationArea.toString())
            binding.txtHeight.text = underline(it.height.toString())
            binding.txtWidth.text = underline(it.width.toString())
            binding.txtLength.text = underline(it.length.toString())
            binding.txtActuatorCount.text = "Actuator (" + it.actuators.size + ")"
            binding.txtSensorCount.text = "Sensor (" + it.sensors.size + ")"
        })
        binding.llType.setOnClickListener(DoubleTapListener(requireContext()) {
            editType()
        })
        binding.llLabel.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.label, "Greenhouse label") { dialog, input ->
                    binding.txtLabel.text = input.text
                    it.label = input.text.toString()
                    dialog.dismiss()
                }
            }
        })
        binding.llArea.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.area.toString(), "Greenhouse area", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtArea.text = input.text
                    it.area = input.text.toString().toFloatOrNull() ?: 0F
                    dialog.dismiss()
                }
            }
        })
        binding.llCultivationArea.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.cultivationArea.toString(), "Greenhouse area", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtCultivationArea.text = input.text
                    it.cultivationArea = input.text.toString().toFloatOrNull() ?: 0F
                    dialog.dismiss()
                }
            }
        })
        binding.llHeight.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.height.toString(), "Greenhouse area", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtHeight.text = input.text
                    it.height = input.text.toString().toFloatOrNull() ?: 0F
                    dialog.dismiss()
                }
            }
        })
        binding.llWidth.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.width.toString(), "Greenhouse area", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtWidth.text = input.text
                    it.width = input.text.toString().toFloatOrNull() ?: 0F
                    dialog.dismiss()
                }
            }
        })
        binding.llLength.setOnClickListener(DoubleTapListener(requireContext()) {
            viewModel.greenhouse.value?.let {
                editText(it.length.toString(), "Greenhouse area", InputType.TYPE_CLASS_NUMBER) { dialog, input ->
                    binding.txtLength.text = input.text
                    it.length = input.text.toString().toFloatOrNull() ?: 0F
                    dialog.dismiss()
                }
            }
        })
    }

    private fun editType() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find greenhouse type", null, GreenhouseType.getModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is GreenhouseTypeModel -> {
                        binding.txtType.text = searchable.greenhouseType.value
                        viewModel.greenhouse.value?.type = searchable.greenhouseType
                        baseSearchDialogCompat.dismiss()
                    }

                    else -> {
                    }
                }
            }
        )
        dialog.show()

    }
}