package tik.prometheus.rest.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tik.prometheus.rest.models.SensorRecord;
import tik.prometheus.rest.models.SensorRecordId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SensorRecordRepos extends JpaRepository<SensorRecord, UUID> {
    @Query(value = """
                SELECT r
                FROM SensorRecord r
                JOIN Sensor s ON s.id = r.sensorId
                WHERE r.sensorId = :sensorId
                AND r.createdAt >= :from
                AND r.createdAt <= :to
            """)
    List<SensorRecord> findSensorRecords(
            @Param("sensorId") Long sensorId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
