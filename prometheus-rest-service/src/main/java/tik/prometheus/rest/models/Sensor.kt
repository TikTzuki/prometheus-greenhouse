package tik.prometheus.rest.models

import tik.prometheus.rest.constants.EUnit
import tik.prometheus.rest.constants.SensorType
import javax.persistence.*

@Entity
class Sensor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var localId: String? = null,
    var address: String? = null,
    @Enumerated(EnumType.STRING)
    var type: SensorType? = SensorType.NaN,
    @Enumerated(EnumType.STRING)
    var unit: EUnit? = EUnit.NaN,
    var label: String? = null,
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "sensor", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var allocation: MutableList<SensorAllocation> = arrayListOf(),
    @OneToMany(mappedBy = "sensor", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var task: MutableList<ActuatorTask> = arrayListOf()
) {

}
