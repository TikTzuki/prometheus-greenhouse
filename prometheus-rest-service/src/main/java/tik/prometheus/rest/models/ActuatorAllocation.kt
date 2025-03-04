package tik.prometheus.rest.models

import javax.persistence.*

@Entity
@IdClass(ActuatorAllocationId::class)
data class ActuatorAllocation(
    @Id
    var greenhouseId: Long? = null,
    @Id
    var actuatorId: Long? = null,
    var north: Float? = null,
    var west: Float? = null,
    var height: Float? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actuatorId", insertable = false, updatable = false)
    var actuator: Actuator? = null,
    @ManyToOne
    @JoinColumn(name = "greenhouseId", insertable = false, updatable = false)
    var greenhouse: Greenhouse? = null
)