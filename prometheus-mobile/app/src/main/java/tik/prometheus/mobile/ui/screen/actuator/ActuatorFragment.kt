package tik.prometheus.mobile.ui.screen.actuator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import tik.prometheus.mobile.NestableFragment
import tik.prometheus.mobile.R
import tik.prometheus.mobile.ZFragment
import tik.prometheus.mobile.databinding.FragmentActuatorBinding
import tik.prometheus.mobile.ui.adapters.ActuatorAdapter
import tik.prometheus.mobile.utils.Utils

class ActuatorFragment : ZFragment(), NestableFragment<ActuatorModel.ActuatorItem> {
    val TAG = ActuatorFragment::class.java.toString()
    private var _binding: FragmentActuatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActuatorViewModel by viewModels { ActuatorViewModel.Factory(zContainer.actuatorRepository) }
    val adapter = ActuatorAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActuatorBinding.inflate(layoutInflater, container, false)
        viewModel.greenhouseId.postValue(requireArguments().getLong(Utils.KEY_GREENHOUSE_ID))

        viewModel.greenhouseId.observe(requireActivity()) {
            viewModel.loadActuators()
            adapter.initAdapter(binding.actuatorsRecyclerView, binding.btnRetry, viewModel.actuators, lifecycleScope)
            adapter.addLoadStateListener(binding.btnRetry, binding.progressBar) {
                showToast(
                    it.error.toString()
                )
            }
        }

        // ACTUATOR

        return binding.root
    }

    override fun insertNestedFragment(model: ActuatorModel.ActuatorItem) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container)
        Log.d(TAG, "insertNestedFragment: "+ model.actuator.id)
        val pair = Pair(Utils.KEY_ACTUATOR_ID, model.actuator.id)
        val args = bundleOf(pair)
        navController.navigate(R.id.nav_actuator_detail, args)
    }

}