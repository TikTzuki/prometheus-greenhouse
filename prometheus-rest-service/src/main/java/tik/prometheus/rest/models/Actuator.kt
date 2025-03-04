package tik.prometheus.rest.models

import tik.prometheus.rest.constants.ActuatorType
import javax.persistence.*

@Entity
class Actuator(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Enumerated(EnumType.STRING)
    var type: ActuatorType? = ActuatorType.NaN,
    var unit: String? = null,
    var localId: String? = null,
    var label: String? = null,
    var isRunning: Boolean = false,
    @OneToMany(mappedBy = "actuator", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var allocation: MutableList<ActuatorAllocation> = arrayListOf(),
    @OneToMany(mappedBy = "actuator", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var task: MutableList<ActuatorTask> = arrayListOf()

)