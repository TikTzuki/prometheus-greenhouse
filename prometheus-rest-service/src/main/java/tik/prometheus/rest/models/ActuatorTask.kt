package tik.prometheus.rest.models

import tik.prometheus.rest.constants.ActuatorTaskType
import java.io.Serializable
import javax.persistence.*


@Entity
class ActuatorTask(
    var actuatorId: Long? = null,
    @Column(nullable = true)
    var sensorId: Long? = null,
    @Enumerated(EnumType.STRING)
    var taskType: ActuatorTaskType? = null,
    var startValue: Float? = null,
    var limitValue: Float? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actuatorId", insertable = false, updatable = false)
    var actuator: Actuator? = null,
    @ManyToOne
    @JoinColumn(name = "sensorId", insertable = false, updatable = false)
    var sensor: Sensor? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;
    class ActuatorTaskId(
        @Id
        var actuatorId: Long? = null,
        @Id
        var sensorId: Long? = null,
    ) : Serializable
}