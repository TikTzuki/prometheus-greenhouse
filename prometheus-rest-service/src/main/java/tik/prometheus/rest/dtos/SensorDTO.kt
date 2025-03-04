package tik.prometheus.rest.dtos

import tik.prometheus.rest.constants.EUnit
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.Sensor
import tik.prometheus.rest.models.SensorAllocation
import tik.prometheus.rest.reflectionToString
import tik.prometheus.rest.services.SensorService

class SensorLiteDTO(
    var id: Long? = null,
    var localId: String? = null,
    var address: String? = null,
    var type: SensorType = SensorType.NaN,
    var unit: EUnit = EUnit.NaN,
    var topic: String? = null,
    var label: String? = null,
) {
    override fun toString() = reflectionToString(this)

}

class SensorDTO(
    var id: Long? = null,
    var localId: String? = null,
    var address: String? = null,
    var type: SensorType = SensorType.NaN,
    var label: String? = null,
    var unit: EUnit = EUnit.NaN,
    var topic: String? = null,
    var north: Float? = null,
    var west: Float? = null,
    var height: Float? = null,
    var greenhouse: GreenhouseLiteDTO? = null,
) {
    override fun toString() = reflectionToString(this)
}

fun Sensor.toSensorLiteDTO() = SensorLiteDTO(
    id = id,
    localId = localId,
    address = address,
    type = type ?: SensorType.NaN,
    unit = unit ?: EUnit.NaN,
    topic = SensorService.sensorTopic(this),
    label = label
)

fun Sensor.toSensorDTO(): SensorDTO {
    val allocation: SensorAllocation? = allocation.firstOrNull()

    return SensorDTO(
        id = id,
        localId = localId,
        address = address,
        type = type ?: SensorType.NaN,
        unit = unit ?: EUnit.NaN,
        topic = SensorService.sensorTopic(this),
        label = label,
        north = allocation?.north,
        west = allocation?.west,
        height = allocation?.height,
        greenhouse = allocation?.greenhouse?.toGreenhouseSummaryDTO()
    )
}

fun SensorAllocation.toSensorLiteDTO() = SensorLiteDTO(
    id = sensor!!.id,
    localId = sensor!!.localId,
    address = sensor!!.address,
    type = sensor!!.type ?: SensorType.NaN,
    unit = sensor!!.unit ?: EUnit.NaN,
    topic = SensorService.sensorTopic(sensor!!),
    label = sensor!!.label
)