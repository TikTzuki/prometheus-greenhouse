package tik.prometheus.rest.dtos

import tik.prometheus.rest.constants.ActuatorType
import tik.prometheus.rest.models.Actuator
import tik.prometheus.rest.models.ActuatorAllocation
import tik.prometheus.rest.reflectionToString
import tik.prometheus.rest.services.ActuatorService
import tik.prometheus.rest.services.GreenhouseService

class ActuatorLiteDTO(
    var id: Long? = null,
    var type: ActuatorType = ActuatorType.NaN,
    var unit: String? = null,
    var localId: String,
    var north: Float? = null,
    var west: Float? = null,
    var height: Float? = null,
    var label: String,
) {

    override fun toString() = reflectionToString(this)
}


class ActuatorDTO(
    var id: Long? = null,
    var type: ActuatorType = ActuatorType.NaN,
    var unit: String? = null,
    var localId: String,
    var greenhouse: GreenhouseLiteDTO? = null,
    var north: Float? = null,
    var west: Float? = null,
    var height: Float? = null,
    var label: String,
    var topic: String?=null,
    var state: ActuatorState = ActuatorState(),
) {
    class ActuatorState(
        var isRunning: Boolean = false,
    )

    override fun toString() = reflectionToString(this)
}

fun ActuatorAllocation.toActuatorLiteDTO(): ActuatorLiteDTO {
    return ActuatorLiteDTO(
        id = actuatorId,
        type = actuator?.type ?: ActuatorType.NaN,
        unit = actuator?.unit,
        localId = actuator?.localId ?: "undefined",
        north = north,
        west = west,
        height = height,
        label = "%s".format(actuator?.localId)
    )
}

fun Actuator.toActuatorDTO(): ActuatorDTO {
    val allo = allocation.firstOrNull()

    val greenhouse = allo?.greenhouse
    val greenhouseDTO = if (greenhouse != null) GreenhouseLiteDTO(
        id = greenhouse.id,
        farmId = greenhouse.farmId!!,
        type = greenhouse.type,
        area = greenhouse.area,
        height = greenhouse.height,
        width = greenhouse.width,
        length = greenhouse.length,
        cultivationArea = greenhouse.cultivationArea,
        label = GreenhouseService.greenhouseLabel(greenhouse)
    ) else null;

    return ActuatorDTO(
        id = id,
        type = type ?: ActuatorType.NaN,
        unit = unit,
        localId = localId ?: "undefined",
        north = allo?.north,
        west = allo?.west,
        height = allo?.height,
        greenhouse = greenhouseDTO,
        label = label ?: "%s".format(localId),
        topic= ActuatorService.actuatorTopic(this),
        state = ActuatorDTO.ActuatorState(
            isRunning = isRunning
        )
    )
}

fun Actuator.toActuatorLiteDTO(): ActuatorLiteDTO {
    val allo = allocation.firstOrNull()
    return ActuatorLiteDTO(
        id = id,
        type = type ?: ActuatorType.NaN,
        unit = unit,
        localId = localId ?: "undefined",
        north = allo?.north,
        west = allo?.west,
        height = allo?.height,
        label = label ?: "%s".format(localId)
    )
}
