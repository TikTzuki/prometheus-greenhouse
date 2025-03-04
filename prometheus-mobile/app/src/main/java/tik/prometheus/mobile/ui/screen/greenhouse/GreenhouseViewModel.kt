package tik.prometheus.mobile.ui.screen.greenhouse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tik.prometheus.mobile.repository.GreenhouseRepository
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.models.Greenhouse

class GreenhouseViewModel(private val greenhouseRepository: GreenhouseRepository) : ViewModel() {
    val selectedType: MutableLiveData<GreenhouseType?> = MutableLiveData(null)
    val selectedLabel: MutableLiveData<String?> = MutableLiveData(null)
    val greenhouses: MutableLiveData<Flow<PagingData<GreenhouseModel>>> = MutableLiveData(null)
    fun postSelectedType(greenhouseType: GreenhouseType?) {
        selectedType.postValue(greenhouseType)
    }

    fun postSelectedLabel(label: String?) {
        selectedLabel.postValue(label)
    }

    fun loadGreenhouses() {
        greenhouses.postValue(greenhouseRepository.getGreenhouseListStream(selectedLabel.value, selectedType.value)
            .map { data ->
                data.map {
                    GreenhouseModel.GreenhouseItem(it)
                }
            }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators GreenhouseModel.SeparatorItem("End of list")
                    } else if (before == null) {
                        return@insertSeparators null
                    } else {
                        null
                    }
                }
            }
        )
    }
    class Factory(val greenhouseRepository: GreenhouseRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GreenhouseViewModel(greenhouseRepository) as T
        }
    }
}

sealed class GreenhouseModel {
    data class GreenhouseItem(val greenhouse: Greenhouse) : GreenhouseModel()
    data class GreenhouseDetail(val greenhouse: Greenhouse) : GreenhouseModel()
    data class SeparatorItem(val description: String) : GreenhouseModel()
}