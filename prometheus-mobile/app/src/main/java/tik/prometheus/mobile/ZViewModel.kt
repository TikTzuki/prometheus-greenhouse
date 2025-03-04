package tik.prometheus.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

abstract class ZViewModel(
    val args: Map<String, Any?> = mutableMapOf()
) : ViewModel()

class ZViewModelFactory(val map: Map<String, Any?>) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Map::class.java).newInstance(map) as T
    }


}
