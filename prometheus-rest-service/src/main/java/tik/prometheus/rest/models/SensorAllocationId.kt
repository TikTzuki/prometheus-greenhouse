package tik.prometheus.rest.models

import java.io.Serializable

class SensorAllocationId(
    var greenhouseId: Long? = null,
    var sensorId: Long? = null,
) : Serializable