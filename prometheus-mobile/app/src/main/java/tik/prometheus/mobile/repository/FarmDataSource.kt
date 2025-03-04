package tik.prometheus.mobile.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tik.prometheus.rest.models.Farm

class FarmDataSource(
    private val restServiceApi: RestServiceApi,
) : PagingSource<Int, Farm>() {
    override fun getRefreshKey(state: PagingState<Int, Farm>): Int? {
        return 0;
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Farm> {
//        val nextPage = params.key ?: 0
//        val res = restServiceApi.getFarms()
        return LoadResult.Page(data = ArrayList(), prevKey = 0, nextKey = 1)
    }


}