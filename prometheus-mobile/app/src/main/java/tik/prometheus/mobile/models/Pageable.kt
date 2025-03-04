package tik.prometheus.mobile.models

class Pageable(
    val page: Int = 0,
    val size: Int = 10,
    val sort: ArrayList<String> = ArrayList(),
) {
    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map["page"] = page.toString()
        map["size"] = size.toString()
//        map["sort"] = sort
        return map
    }
}