package tik.prometheus.rest.models

import javax.persistence.*

@Entity
@Table(name = "farm")
class Farm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var region: String? = null,
    var areOfFarm: Float? = null,
    var numberOfGreenhouse: String? = null,
    var label: String? = null
) {
}