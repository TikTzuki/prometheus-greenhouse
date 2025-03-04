package tik.prometheus.rest.dtos

import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.models.ActuatorAllocation
import tik.prometheus.rest.models.Greenhouse
import tik.prometheus.rest.models.SensorAllocation
import tik.prometheus.rest.services.GreenhouseService

class GreenhouseLiteDTO(
    var id: Long? = null,
    var farmId: Long? = null,
    var type: GreenhouseType,
    var label: String?,
    var area: Float,
    var height: Float,
    var width: Float,
    var length: Float,
    var cultivationArea: Float,
)

class GreenhouseDTO(
    var id: Long? = null,
    var farmId: Long? = null,
    var type: GreenhouseType,
    var area: Float,
    var height: Float,
    var width: Float,
    var length: Float,
    var cultivationArea: Float,
    var actuators: List<ActuatorLiteDTO> = emptyList(),
    var sensors: List<SensorLiteDTO> = emptyList(),
    var label: String? = null,
)

fun Greenhouse.toGreenhouseSummaryDTO(): GreenhouseLiteDTO {
    return GreenhouseLiteDTO(
        id = id,
        farmId = farmId,
        type = type,
        area = area,
        height = height,
        width = width,
        length = length,
        cultivationArea = cultivationArea,
        label = label ?: GreenhouseService.greenhouseLabel(this)
    )
}

fun Greenhouse.toGreenhouseDTO(): GreenhouseDTO {
    return GreenhouseDTO(
        id = id,
        farmId = farmId,
        type = type,
        area = area,
        height = height,
        width = width,
        length = length,
        cultivationArea = cultivationArea,
        label = label,
        actuators = actuatorAllocations.map(ActuatorAllocation::toActuatorLiteDTO),
        sensors = sensorAllocations.map(SensorAllocation::toSensorLiteDTO),
    )
}