package tik.prometheus.mobile.ui.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tik.prometheus.mobile.models.Pageable
import tik.prometheus.mobile.repository.SensorRepository
import tik.prometheus.mobile.ui.screen.sensor.SensorModel
import tik.prometheus.rest.constants.NullableSensorTypeModel
import tik.prometheus.rest.models.Greenhouse
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.streams.toList


class HomeViewModel(private val sensorRepository: SensorRepository) : ViewModel() {
    val TAG = HomeViewModel::class.toString()

    val sensors: MutableLiveData<Flow<PagingData<SensorModel>>> = MutableLiveData(null)
    var selectSensors: MutableLiveData<MutableMap<Long, SensorModel.SensorItem>> = MutableLiveData(mutableMapOf())
    var sensorLineChartData: MutableLiveData<MutableList<ILineDataSet>> = MutableLiveData(ArrayList())
    var fromDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now().plusDays(-7))
    var toDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now().plusDays(1))
    var selectedGreenhouse: MutableLiveData<Greenhouse> = MutableLiveData(null)
    var selectedSensorType: MutableLiveData<NullableSensorTypeModel> = MutableLiveData(null)
    var greenhouses: ArrayList<Greenhouse> = ArrayList()
    fun postFromDate(from: LocalDate) {
        fromDate.postValue(from)
        loadSensorLineChartData(arrayListOf())
    }

    fun postToDate(to: LocalDate) {
        toDate.postValue(to)
        loadSensorLineChartData(arrayListOf())
    }

    fun postSelectedSensorType(sensorType: NullableSensorTypeModel) {
        selectedSensorType.postValue(sensorType)
    }
    fun postSelectedGreenhouse(greenhouse: Greenhouse) {
        selectedGreenhouse.postValue(greenhouse)
    }

    fun loadSensors() {
        println("loadSensors %s %s".format(selectedGreenhouse.value, selectedSensorType.value?.sensorType))
        sensors.postValue(sensorRepository.getSensorListStream(greenhouseId = selectedGreenhouse.value?.id, sensorType = selectedSensorType.value?.sensorType)
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
            })
    }

    fun loadGreenhouse() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = sensorRepository.restServiceApi.getGreenhouses(Pageable(page = 0, size = 100).toMap())
            if (res.isSuccessful) {
                res.body()?.content?.let {
                    greenhouses.clear()
                    greenhouses.addAll(it)
                }
            }
        }
    }

    fun loadSensorLineChartData(colorItr: ArrayList<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            val data: MutableList<ILineDataSet> = arrayListOf()
            for (sensorId in selectSensors.value!!.keys) {
                val from = fromDate.value!!
                val to = toDate.value!!
                val res = sensorRepository.restServiceApi.getSensorRecords(
                    sensorId,
                    LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0, 0),
                    LocalDateTime.of(to.year, to.month, to.dayOfMonth, 0, 0, 0)
                )
                if (res.isSuccessful) {
                    var i = -1f
                    val dataSet = LineDataSet(
                        res.body()!!.stream().map { i++; Entry(i, it) }.toList(),
                        sensorId.toString()
                    )
//                    dataSet.color = colorItr[i.toInt() % colorItr.size]
                    println(dataSet)
                    data.add(dataSet)
                }
            }
            sensorLineChartData.postValue(data)
        }
    }



    class Factory(val sensorRepository: SensorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(sensorRepository) as T
        }
    }
}