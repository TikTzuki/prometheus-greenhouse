package tik.prometheus.mobile.ui.screen.sensor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import ir.mirrajabi.searchdialog.core.Searchable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.eclipse.paho.android.service.MqttAndroidClient
import tik.prometheus.mobile.ZViewModel
import tik.prometheus.mobile.models.Sensor
import tik.prometheus.mobile.repository.SensorRepository


class SensorViewModel(private val sensorRepository: SensorRepository) : ZViewModel() {
    val greenhouseId: MutableLiveData<Long> = MutableLiveData(null)

    lateinit var sensors: Flow<PagingData<SensorModel>>

    fun loadSensors() {
        sensors = sensorRepository.getSensorListStream(greenhouseId = this.greenhouseId.value)
            .map { data ->
                data.map {
                    SensorModel.SensorItem(it)
                }
            }
            .map {
                it.insertSeparators<SensorModel.SensorItem, SensorModel>() { before, after ->
                    if (after == null) {
                        return@insertSeparators SensorModel.SeparatorItem("End of list")
                    } else if (before == null) {
                        return@insertSeparators null
                    } else {
                        null
                    }
                }
            }
    }

    fun postGreenhouseId(greenhouseId:Long){
        this.greenhouseId.postValue(greenhouseId)
    }

    class Factory(val sensorRepository: SensorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SensorViewModel(sensorRepository) as T
        }
    }
}

sealed class SensorModel {
    data class SensorItem(val sensor: Sensor, var mqttClient: MqttAndroidClient? = null) : SensorModel(), Searchable {
        override fun getTitle(): String {
            return "%s. %s".format(sensor.id, sensor.label)
        }
    }

    data class SeparatorItem(val description: String) : SensorModel()
}
