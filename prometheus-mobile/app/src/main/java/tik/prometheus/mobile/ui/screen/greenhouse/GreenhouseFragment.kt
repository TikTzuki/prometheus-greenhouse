package tik.prometheus.mobile.ui.screen.greenhouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentGreenhouseBinding
import tik.prometheus.mobile.ui.adapters.GreenhouseAdapter
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.mobile.utils.underline
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.constants.NullableGreenhouseTypeModel

class GreenhouseFragment : ZFragment(), NestableFragment<GreenhouseModel.GreenhouseItem> {
    private val TAG = GreenhouseFragment::class.toString()

    private val viewModel: GreenhouseViewModel by viewModels { GreenhouseViewModel.Factory(zContainer.greenhouseRepository) }
    private var _binding: FragmentGreenhouseBinding? = null
    private val binding get() = _binding!!
    private val greenhouseAdapter = GreenhouseAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGreenhouseBinding.inflate(layoutInflater, container, false)

        viewModel.greenhouses.observe(requireActivity()) {
            it?.let {
                greenhouseAdapter.initAdapter(binding.rvGreenhouse, binding.btnRetry, it, lifecycleScope)
                greenhouseAdapter.addLoadStateListener(binding.btnRetry, binding.progressBar)
            }
        }
        setupSearcher()
        return binding.root
    }

    fun setupSearcher() {
        viewModel.selectedLabel.observe(requireActivity()) {
            var labelText = it ?: "Label"
            binding.txtSearchLabel.text = underline(labelText)
            viewModel.loadGreenhouses()
        }
        binding.txtSearchLabel.setOnClickListener {
            editText(viewModel.selectedLabel.value, "Greenhouse label") { dialog, input ->
                val label = if (input.text.isNotBlank()) input.text.toString() else null
                viewModel.postSelectedLabel(label)
                dialog.dismiss()
            }
        }
        viewModel.selectedType.observe(requireActivity()) {
            var type = it?.name ?: "Type"
            binding.txtSearchGreenhouseType.text = underline(type)
            viewModel.loadGreenhouses()
        }
        binding.txtSearchGreenhouseType.setOnClickListener {
            editType()
        }

    }

    private fun editType() {
        val dialog = SimpleSearchDialogCompat(context, "Search", "Find greenhouse type", null, GreenhouseType.getNullableModels(),
            SearchResultListener { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, i: Int ->
                when (searchable) {
                    is NullableGreenhouseTypeModel -> {
                        viewModel.postSelectedType(searchable.type)
                    }

                    else -> {
                    }
                }
                baseSearchDialogCompat.dismiss()
            }
        )
        dialog.show()
    }

    override fun insertNestedFragment(model: GreenhouseModel.GreenhouseItem) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        val args = bundleOf(Utils.KEY_GREENHOUSE_ID to model.greenhouse.id)
        navController.navigate(R.id.nav_greenhouse_detail, args);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}