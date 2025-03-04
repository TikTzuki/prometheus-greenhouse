package tik.prometheus.mobile.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tik.prometheus.mobile.models.Actuator
import tik.prometheus.mobile.models.Pageable
import tik.prometheus.mobile.utils.toExcept

class ActuatorDataSource(
    private val restServiceApi: RestServiceApi,
    var greenhouseId: Long? = null
) : PagingSource<Int, Actuator>() {

    override fun getRefreshKey(state: PagingState<Int, Actuator>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actuator> {
        val nextPage = params.key ?: 0

        val res = restServiceApi.getActuators(Pageable(page = nextPage, size = 10).toMap(), greenhouseId)
        if (res.isSuccessful) {
            val page = res.body()
            return LoadResult.Page(
                data = page?.content!!,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (nextPage < page.totalPages) page.pageable.pageNumber.plus(1) else null
            )
        } else {
            val except = res.toExcept()
            return LoadResult.Error(Exception(except.message))
        }
    }
}