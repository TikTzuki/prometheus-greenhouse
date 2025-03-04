package tik.prometheus.mobile.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tik.prometheus.mobile.models.Sensor
import tik.prometheus.rest.constants.SensorType

class SensorRepository(val restServiceApi: RestServiceApi = RestServiceHelper.createApi()) {

    fun getSensorListStream(greenhouseId: Long? = null, sensorType: SensorType? = null): Flow<PagingData<Sensor>> {
        return Pager(PagingConfig(20)) { SensorDataSource(restServiceApi, greenhouseId, sensorType) }.flow
    }
}