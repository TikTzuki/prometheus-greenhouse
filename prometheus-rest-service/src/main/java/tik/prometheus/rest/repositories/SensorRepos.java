package tik.prometheus.rest.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tik.prometheus.rest.constants.SensorType;
import tik.prometheus.rest.models.Sensor;

import java.util.Optional;

@Repository
public interface SensorRepos extends JpaRepository<Sensor, Long> {
    @Query("""
            SELECT s
            FROM Sensor s
            LEFT JOIN SensorAllocation sa ON s.id = sa.sensorId
            WHERE (sa.greenhouseId = :greenhouseId or :greenhouseId is null)
            AND (s.type = :type or :type is null)
            """)
    Page<Sensor> findAllWithParams(@Param("greenhouseId") Long greenhouseId, @Param("type") SensorType sensorType, Pageable pageable);

    @Query("""
            SELECT s
            FROM Sensor s
            LEFT JOIN SensorRecord sr ON sr.sensorId = s.id
            WHERE sr.id = :recordId
            """)
    Optional<Sensor> findByRecord(@Param("recordId") Long recordId);
}
