package tik.prometheus.rest.models

import ir.mirrajabi.searchdialog.core.Searchable
import tik.prometheus.mobile.models.Actuator
import tik.prometheus.mobile.models.Sensor
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.rest.constants.GreenhouseType


class Greenhouse(
    var id: Long,
    var farmId: Long? = null,
    var type: GreenhouseType? = GreenhouseType.NaN,
    var area: Float? = Float.NaN,
    var height: Float? = Float.NaN,
    var width: Float? = Float.NaN,
    var length: Float? = Float.NaN,
    var cultivationArea: Float? = Float.NaN,
    var label: String? = null,
    var sensors: List<Sensor> = emptyList(),
    var actuators: List<Actuator> = emptyList()
) : Searchable {
    override fun toString(): String = Utils.reflectionToString(this)
    override fun getTitle(): String {
        return label ?: "null"
    }
}

class GreenhouseReq(
    var id: Long? = null,
    var farmId: Long? = null,
    var type: GreenhouseType,
    var area: Float,
    var label: String? = null,
    var height: Float,
    var width: Float,
    var length: Float,
    var cultivationArea: Float,
    var actuators: List<Any> = emptyList(),
    var sensors: List<Any> = emptyList()
) {
    override fun toString(): String = Utils.reflectionToString(this)
}

fun Greenhouse.toGreenhouseReq() = GreenhouseReq(
    type = type ?: GreenhouseType.NaN,
    area = area ?: 0F,
    cultivationArea = cultivationArea ?: 0F,
    label = label,
    height = height ?: 0F,
    width = width ?: 0F,
    length = length ?: 0F
)
