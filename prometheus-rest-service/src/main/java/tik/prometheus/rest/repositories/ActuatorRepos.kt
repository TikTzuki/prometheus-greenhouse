package tik.prometheus.rest.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tik.prometheus.rest.models.Actuator
import tik.prometheus.rest.models.ActuatorTask

@Repository
interface ActuatorRepos : JpaRepository<Actuator, Long> {
    @Query(
        """
        SELECT a 
        FROM Actuator a
        LEFT JOIN ActuatorAllocation aa ON a.id = aa.actuatorId
        LEFT JOIN Greenhouse gh ON gh.id = aa.greenhouseId
        WHERE gh.id = :greenhouseId or :greenhouseId is null"""
    )
    fun findAllWithParams(@Param("greenhouseId") greenhouseId: Long? = null, pageable: Pageable): Page<Actuator>

    //        LEFT JOIN Sensor s ON s.id = t.sensorId
    @Query(
        """
        SELECT t
        FROM ActuatorTask t
        LEFT JOIN SensorRecord sr ON sr.sensorId = t.sensorId
        WHERE sr.id = :id
    """
    )
    fun findActuatorTaskBySensorRecord(@Param("id") id: Long): List<ActuatorTask>
}