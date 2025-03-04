package tik.prometheus.rest.models

import tik.prometheus.rest.constants.GreenhouseType
import javax.persistence.*

@Entity
@Table(name = "greenhouse")
class Greenhouse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var farmId: Long? = null,
    @Enumerated(EnumType.STRING)
    var type: GreenhouseType = GreenhouseType.NORMAL,
    var area: Float = 0f,
    var height: Float = 0f,
    var width: Float = 0f,
    var length: Float = 0f,
    var cultivationArea: Float = 0f,
    var label: String? = null
) {

    @OneToMany(mappedBy = "greenhouse")
    var actuatorAllocations: List<ActuatorAllocation> = emptyList()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name="greenhouseId")
    var sensorAllocations: List<SensorAllocation> = mutableListOf()

}