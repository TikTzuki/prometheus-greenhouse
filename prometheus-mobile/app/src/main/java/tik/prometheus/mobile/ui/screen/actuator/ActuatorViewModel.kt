package tik.prometheus.mobile.ui.screen.actuator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tik.prometheus.mobile.ZViewModel
import tik.prometheus.mobile.models.Actuator
import tik.prometheus.mobile.repository.ActuatorRepository

class ActuatorViewModel(private val actuatorRepository: ActuatorRepository) : ZViewModel() {
    val greenhouseId: MutableLiveData<Long> = MutableLiveData()
    lateinit var actuators: Flow<PagingData<ActuatorModel>>

    fun loadActuators() {
        actuators = actuatorRepository.getActuatorListStream(greenhouseId.value)
            .map { data ->
                data.map { ActuatorModel.ActuatorItem(it) }
            }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators ActuatorModel.SeparatorItem("End of list")
                    } else if (before == null) {
                        return@insertSeparators null
                    } else {
                        null
                    }
                }
            }
    }

    class Factory(val actuatorRepository: ActuatorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActuatorViewModel(actuatorRepository) as T
        }
    }

}

sealed class ActuatorModel {
    data class ActuatorItem(val actuator: Actuator) : ActuatorModel()
    data class SeparatorItem(val description: String) : ActuatorModel()
}
