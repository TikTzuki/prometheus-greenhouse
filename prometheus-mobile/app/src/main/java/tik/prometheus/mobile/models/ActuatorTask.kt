package tik.prometheus.mobile.models

import tik.prometheus.mobile.utils.ActuatorTaskType
import tik.prometheus.mobile.utils.EUnit
import tik.prometheus.mobile.utils.Utils
import tik.prometheus.rest.constants.SensorType

class ActuatorTask(
    var sensorId: Long? = null,
    var taskType: ActuatorTaskType,
    var sensorTopic: String? = null,
    var sensorType: SensorType = SensorType.NaN,
    var sensorUnit: EUnit = EUnit.NaN,
    var sensorLabel: String? = null,
    var startValue: Float,
    var limitValue: Float,
) {
    override fun toString(): String = Utils.reflectionToString(this)
}