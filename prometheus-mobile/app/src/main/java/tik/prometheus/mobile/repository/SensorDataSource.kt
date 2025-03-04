package tik.prometheus.mobile.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tik.prometheus.mobile.models.Pageable
import tik.prometheus.mobile.models.Sensor
import tik.prometheus.mobile.utils.toExcept
import tik.prometheus.rest.constants.SensorType

class SensorDataSource(
    private val restServiceApi: RestServiceApi,
    var greenhouseId: Long? = null,
    var sensorType: SensorType? = null,
) : PagingSource<Int, Sensor>() {
    val TAG = SensorDataSource::class.toString()
    override fun getRefreshKey(state: PagingState<Int, Sensor>): Int {
        return 0;
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Sensor> {
        val nextPage = params.key ?: 0
        val res = restServiceApi.getSensors(Pageable(page = nextPage, size = 10).toMap(), greenhouseId, sensorType)
        if (res.isSuccessful) {
            val pageSensor = res.body()
            return LoadResult.Page(
                data = pageSensor?.content!!,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (nextPage < pageSensor.totalPages) pageSensor.pageable.pageNumber.plus(1) else null
            )
        } else {
            val except = res.toExcept()
            return LoadResult.Error(Exception(except.message))
        }
    }


}
