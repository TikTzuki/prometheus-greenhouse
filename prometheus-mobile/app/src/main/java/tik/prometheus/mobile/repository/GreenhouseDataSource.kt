package tik.prometheus.mobile.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import tik.prometheus.mobile.models.Pageable
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.models.Greenhouse

class GreenhouseDataSource(
    private val restServiceApi: RestServiceApi,
    var label: String? = null,
    var type: GreenhouseType? = null,
) : PagingSource<Int, Greenhouse>() {
    val TAG = GreenhouseDataSource::class.toString()

    override fun getRefreshKey(state: PagingState<Int, Greenhouse>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Greenhouse> {
        val nextPage = params.key ?: 0
        val res = restServiceApi.getGreenhouses(Pageable(page = nextPage, size = 10).toMap(), label, type)
        if (res.isSuccessful) {
            val page = res.body()
            Log.d(TAG, "load: " + page.toString())
            return LoadResult.Page(
                data = page?.content!!,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (nextPage < page.totalPages) page.pageable.pageNumber.plus(1) else null
            )
        } else {
            Log.d(TAG, "load: failed")
            return LoadResult.Error(Exception(res.errorBody().toString()))
        }
    }

}