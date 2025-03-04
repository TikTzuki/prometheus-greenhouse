package tik.prometheus.rest.dtos

import tik.prometheus.rest.constants.ActuatorTaskType
import tik.prometheus.rest.constants.EUnit
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.ActuatorTask
import tik.prometheus.rest.services.SensorService

class ActuatorTaskDTO(
    var sensorId: Long,
    var taskType: ActuatorTaskType,
    var sensorTopic: String?=null,
    var sensorType: SensorType = SensorType.NaN,
    var sensorUnit: EUnit = EUnit.NaN,
    var sensorLabel: String? = null,
    var startValue: Float,
    var limitValue: Float,
)

fun ActuatorTask.toDTO(): ActuatorTaskDTO {
    val task = ActuatorTaskDTO(
        sensorId = sensorId!!,
        taskType = taskType!!,
        startValue = startValue!!,
        limitValue = limitValue!!,
    )
    sensor?.let {
        task.sensorType = it.type ?: SensorType.NaN
        task.sensorUnit = it.unit ?: EUnit.NaN
        task.sensorLabel = it.label
        task.sensorTopic = SensorService.sensorTopic(it)
    }
    return task
}