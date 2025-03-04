package tik.prometheus.mobile.models

import tik.prometheus.mobile.utils.EUnit
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.Greenhouse

class Sensor(
    var id: Long,
    var localId: String,
    var address: String?,
    var type: SensorType?,
    var label: String?,
    var unit: EUnit?,
    var topic: String,
    var north: Float,
    var west: Float,
    var height: Float,
    var greenhouse: Greenhouse
) {
    override fun toString(): String = Utils.reflectionToString(this)
}

class SensorReq(
    var localId: String,
    var address: String?,
    var type: SensorType?,
    var label: String?,
    var unit: EUnit?,
    var topic: String,
    var north: Float,
    var west: Float,
    var height: Float,
    var greenhouse: Greenhouse
)

fun Sensor.toSensorReq() = SensorReq(
    localId = localId,
    address = address,
    type = type,
    label = label,
    unit = unit,
    topic = topic,
    north = north,
    west = west,
    height = height,
    greenhouse = greenhouse
)