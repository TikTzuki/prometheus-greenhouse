package tik.prometheus.rest.models

import java.io.Serializable
import javax.persistence.Id

class ActuatorAllocationId(
    @Id
    var greenhouseId: Long? = null,
    @Id
    var actuatorId: Long? = null,
) : Serializable