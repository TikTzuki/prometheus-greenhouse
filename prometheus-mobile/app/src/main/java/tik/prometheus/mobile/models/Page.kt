package tik.prometheus.mobile.models

import tik.prometheus.mobile.utils.Utils
import tik.prometheus.rest.models.Farm

class Page<T>(
    var content: List<T>? = ArrayList(),
    var pageable: PageableRes,
    var totalPages: Int = 0,
    var totalElements: Int = 0,
    var size: Int = 1,
    var first: Boolean = true,
    var last: Boolean = false,
    var numberOfElements: Int = 0,
    var empty: Boolean = true,
) {
    class PageableRes(
        var offset: Int = 0,
        var pageNumber: Int = 0,
        var pageSize: Int = 10,
    )
    override fun toString(): String = Utils.reflectionToString(this)
}