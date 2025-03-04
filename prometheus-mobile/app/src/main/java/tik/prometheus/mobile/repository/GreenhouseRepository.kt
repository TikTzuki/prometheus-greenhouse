package tik.prometheus.mobile.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.models.Greenhouse

class GreenhouseRepository(val restServiceApi: RestServiceApi=RestServiceHelper.createApi()) {
    fun getGreenhouseListStream(label: String? = null, type: GreenhouseType? = null): Flow<PagingData<Greenhouse>> {
        return Pager(PagingConfig(20)) {
            GreenhouseDataSource(restServiceApi, label, type)
        }.flow
    }

}