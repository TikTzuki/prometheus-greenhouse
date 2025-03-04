package tik.prometheus.mobile.ui.screen.sensor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tik.prometheus.mobile.ZViewModel
import tik.prometheus.mobile.models.Sensor
import tik.prometheus.mobile.models.toSensorReq
import tik.prometheus.mobile.repository.SensorRepository
import tik.prometheus.mobile.utils.ZLoadState
import tik.prometheus.mobile.utils.toExcept


class SensorDetailViewModel(private val sensorRepository: SensorRepository) : ZViewModel() {
    var sensorId: MutableLiveData<Long> = MutableLiveData()

    var sensor: MutableLiveData<Sensor> = MutableLiveData()
    var loadState: MutableLiveData<ZLoadState> = MutableLiveData(ZLoadState.NOT_LOADING)


    fun loadSensor() {
        var state = ZLoadState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val res = sensorRepository.restServiceApi.getSensor(sensorId.value!!)
            println(res.body())
            if (res.isSuccessful) {
                sensor.postValue(res.body())
            }
        }
    }

    fun saveSensor() {
        loadState.postValue(ZLoadState.LOADING)
        println(sensor.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val res = sensorRepository.restServiceApi.putSensor(sensorId.value!!, sensor.value!!.toSensorReq())
            if (res.isSuccessful) {
                loadState.postValue(ZLoadState.NOT_LOADING)
                sensor.postValue(res.body())
            } else {
                val except = res.toExcept()
                loadState.postValue(ZLoadState.Error(Exception(except.message)))
            }
        }
    }

    class Factory(val sensorRepository: SensorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SensorDetailViewModel(sensorRepository) as T
        }
    }
}