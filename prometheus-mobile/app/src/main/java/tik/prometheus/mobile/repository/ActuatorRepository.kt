package tik.prometheus.mobile.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tik.prometheus.mobile.models.Actuator

class ActuatorRepository(val restServiceApi: RestServiceApi = RestServiceHelper.createApi()) {
    private val TAG = ActuatorRepository::class.toString()

    fun getActuatorListStream(greenhouseId: Long? = null): Flow<PagingData<Actuator>> {
        return Pager(PagingConfig(20)) { ActuatorDataSource(restServiceApi, greenhouseId)}.flow
    }

}