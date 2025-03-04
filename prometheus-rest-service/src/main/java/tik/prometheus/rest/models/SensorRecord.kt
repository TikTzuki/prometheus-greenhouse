package tik.prometheus.rest.models

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "SENSOR_RECORD")
class SensorRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var greenhouseId: Long? = null,
    var sensorId: Long? = null,
    var createdAt: LocalDateTime? = null,
    var weather: String? = null,
    var numberOfWeek: String? = null,
    var sensorData: String? = null,
    var lineNumber: String? = null,
    @Column(name = "UPDATE_TS")
    var updateTs: Timestamp? = null,
)