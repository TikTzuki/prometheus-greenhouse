package tik.prometheus.rest.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class NutrientIrrigatorRecord(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id: Long?=null,
    var actuatorId: Long? = null,
    var runDate: LocalDateTime? = null,
//    var lineNumber: String? = null,
//    var weather: String? = null,
//    var numberOfWeek: Int? = null,
//    var singleSupply: String? = null,
//    var ec: String? = null,
) {

}