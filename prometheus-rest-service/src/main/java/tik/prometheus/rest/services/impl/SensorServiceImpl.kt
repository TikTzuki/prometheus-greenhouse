package tik.prometheus.rest.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.dtos.SensorDTO
import tik.prometheus.rest.dtos.SensorLiteDTO
import tik.prometheus.rest.dtos.toSensorDTO
import tik.prometheus.rest.dtos.toSensorLiteDTO
import tik.prometheus.rest.models.Sensor
import tik.prometheus.rest.models.SensorAllocation
import tik.prometheus.rest.repositories.GreenhouseRepos
import tik.prometheus.rest.repositories.SensorRecordRepos
import tik.prometheus.rest.repositories.SensorRepos
import tik.prometheus.rest.services.SensorService
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

@Service
class SensorServiceImpl @Autowired constructor(
    private val repos: SensorRepos,
    private val recordRepos: SensorRecordRepos,
    private val greenhouseRepos: GreenhouseRepos,
    private val entityManager: EntityManager
) : SensorService {
    override fun getSensors(pageable: Pageable, greenhouseId: Long?, type: SensorType?): Page<SensorLiteDTO> {
        val pageEntity = repos.findAllWithParams(greenhouseId, type, pageable)
        return pageEntity.map(Sensor::toSensorLiteDTO)
    }

    override fun getSensor(id: Long): SensorDTO {
        return repos.findById(id)
            .map(Sensor::toSensorDTO)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "No result found") }
    }

    override fun updateSensor(sensorId: Long, sensorDto: SensorDTO): SensorDTO {
        val sensor = repos.findById(sensorId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        sensor.type = sensorDto.type
        sensor.localId = sensorDto.localId
        sensor.address = sensorDto.address
        sensor.label = sensorDto.label
        sensor.unit = sensorDto.unit
        sensor.allocation.clear()

        if (sensorDto.greenhouse != null) {
            val greenhouse = greenhouseRepos.getReferenceById(sensorDto.greenhouse!!.id!!)
            sensor.allocation.add(
                SensorAllocation(
                    greenhouseId = greenhouse.id,
                    sensorId = sensor.id,
                    greenhouse = greenhouse,
                    north = sensorDto.north,
                    west = sensorDto.west,
                    height = sensorDto.height
                )
            )
        }
        repos.save(sensor)
        return sensor.toSensorDTO()
    }

    override fun deleteSensor(sensorId: Long) {
        repos.deleteById(sensorId)
    }


    override fun getSensorRecords(id: Long, from: LocalDateTime, to: LocalDateTime): List<Float> {
        val content = recordRepos.findSensorRecords(id, from, to)
        val map: MutableMap<LocalDate, ArrayList<Float>> = mutableMapOf()
        var dateCursor = from.toLocalDate()
        val end = to.toLocalDate()
        while (dateCursor < end) {
            map.put(
                LocalDate.of(dateCursor.year, dateCursor.month, dateCursor.dayOfMonth),
                ArrayList()
            )
            dateCursor = dateCursor.plusDays(1)
        }
        for (record in content) {
            try {
                val value = record.sensorData.toString().toFloat()
                val recordDate = record.createdAt!!.toLocalDate()
                if (map[recordDate] == null) {
                    map[recordDate] = arrayListOf(value)
                } else {
                    map[recordDate]!!.add(value)
                }
            } catch (e: Exception) {
                continue
            }
        }
        return map.values.stream().map {
            var rs = it.firstOrNull()
            if (rs == null) {
                rs = 0f
            }
            rs
        }.toList()
    }

}

