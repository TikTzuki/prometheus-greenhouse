package tik.prometheus.rest.models

import javax.persistence.*

@Entity
@IdClass(SensorAllocationId::class)
@Table(name = "sensor_allocation")
class SensorAllocation(
    @Id
    var greenhouseId: Long? = null,
    @Id
    var sensorId: Long? = null,

    var north: Float? = null,
    var west: Float? = null,
    var height: Float? = null,
    @ManyToOne
    @JoinColumn(name = "sensorId", updatable = false, insertable = false)
    var sensor: Sensor? = null,

    @ManyToOne
    @JoinColumn(name = "greenhouseId", updatable = false, insertable = false)
    var greenhouse: Greenhouse? = null
)
