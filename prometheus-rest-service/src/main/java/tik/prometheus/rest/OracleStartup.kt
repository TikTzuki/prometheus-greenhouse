package tik.prometheus.rest

import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import tik.prometheus.rest.constants.ActuatorTaskType
import tik.prometheus.rest.constants.ActuatorType
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.constants.SensorValueGroup
import tik.prometheus.rest.dtos.RangeDTO
import tik.prometheus.rest.models.*
import tik.prometheus.rest.repositories.*
import java.time.Duration
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class OracleStartup @Autowired constructor(
    val sensorRepos: SensorRepos,
    val greenhouseRepos: GreenhouseRepos,
    val sensorRecordRepos: SensorRecordRepos,
    val farmRepos: FarmRepos,
    val sensorTypeMetadataRepos: SensorTypeMetadataRepos,
    val irrigatorRecordRepos: IrrigatorRecordRepos,
    val actuatorRepos: ActuatorRepos
) : InitializingBean {
    val log = LoggerFactory.getLogger(OracleStartup::class.java)

    override fun afterPropertiesSet() {

        if (farmRepos.findAll(Example.of(Farm(label = "sample"))).size == 0) {
            log.info("Run startup oracle script")
            createSampleData()
        }

//        mockData()

//        createMetadata()
    }

    private fun createSampleData() {
        val farm = Farm(label = "sample")
        farmRepos.save(farm)

        val sampleGreenhouse = Greenhouse(farmId = farm.id, label = "sample")
        greenhouseRepos.save(sampleGreenhouse)
        val defaultGreenhouse = Greenhouse(farmId = farm.id, label = "default")
        greenhouseRepos.save(defaultGreenhouse)


        listOf(SensorType.HUMIDITY, SensorType.TEMPERATURE, SensorType.WATER, SensorType.SOIL_MOISTURE).forEach {
            val sensor = Sensor(
                localId = "00:00:00:00:00/%s".format(it.value),
                address = "sample",
                label = "sample",
                type = it
            )
            sensorRepos.save(sensor)

            sensor.allocation.add(
                SensorAllocation(
                    greenhouseId = sampleGreenhouse.id,
                    sensorId = sensor.id
                )
            )
            sensorRepos.save(sensor)
        }

        val actuator = Actuator(
            localId = "00:00:00:00:00/pump",
            type = ActuatorType.IRRIGATOR,
            label = "sample",
        )
        actuatorRepos.save(actuator)
        actuator.allocation = mutableListOf(ActuatorAllocation(greenhouseId = sampleGreenhouse.id, actuatorId = actuator.id))
        actuator.task = mutableListOf(ActuatorTask(taskType = ActuatorTaskType.DECISION, actuatorId = actuator.id))
        actuatorRepos.save(actuator)

    }

    private fun mockData() {
        val actuator = actuatorRepos.findAll(Example.of(Actuator(type = ActuatorType.IRRIGATOR, label = "sample")))
        actuator.forEach {
            var cursor = LocalDateTime.now() - Duration.ofHours(168)
            while (cursor < LocalDateTime.now()) {
                val rand = Random(System.currentTimeMillis())
                val randValue = rand.nextInt(0, 10)
                val mod = randValue % 2
                println("%s %s".format(randValue, mod))
                if (mod == 1) {
                    createIrrigatorRecord(it, cursor)
                }
                cursor += Duration.ofHours(1)
            }
        }

        val sampleGreenhouse = greenhouseRepos.findAll(Example.of(Greenhouse(label = "sample")))[0]
        val sensors = sensorRepos.findAll(Example.of(Sensor(type = null, label = "sample")))
        sensors.forEach {
            var cursor = LocalDateTime.now() - Duration.ofHours(168)
            while (cursor < LocalDateTime.now()) {
                createSensorRecord(it, sampleGreenhouse, cursor)
                cursor += Duration.ofHours(1)
            }
        }

    }

    private fun createMetadata() {

        if (sensorTypeMetadataRepos.findAll(
                Example.of(SensorTypeMetadata(type = SensorType.SOIL_MOISTURE))
            ).isEmpty()
        ) {
            sensorTypeMetadataRepos.save(
                SensorTypeMetadata(
                    type = SensorType.SOIL_MOISTURE,
                    content = mutableMapOf(
                        SensorValueGroup.LOW.name to RangeDTO(SensorValueGroup.NEGATIVE_INFINITY, 150F),
                        SensorValueGroup.HIGH.name to RangeDTO(151F, SensorValueGroup.POSITIVE_INFINITY),
                    )
                )
            )
        }

        if (sensorTypeMetadataRepos.findAll(
                Example.of(SensorTypeMetadata(type = SensorType.WATER))
            ).isEmpty()
        ) {
            sensorTypeMetadataRepos.save(
                SensorTypeMetadata(
                    type = SensorType.WATER,
                    content = mutableMapOf(
                        SensorValueGroup.LOW.name to RangeDTO(SensorValueGroup.NEGATIVE_INFINITY, 150F),
                        SensorValueGroup.HIGH.name to RangeDTO(151F, SensorValueGroup.POSITIVE_INFINITY),
                    )
                )
            )

        }

        if (sensorTypeMetadataRepos.findAll(
                Example.of(SensorTypeMetadata(type = SensorType.TEMPERATURE))
            ).isEmpty()
        ) {
            sensorTypeMetadataRepos.save(
                SensorTypeMetadata(
                    type = SensorType.TEMPERATURE,
                    content = mutableMapOf(
                        SensorValueGroup.LOW.name to RangeDTO(SensorValueGroup.NEGATIVE_INFINITY, 150F),
                        SensorValueGroup.HIGH.name to RangeDTO(151F, SensorValueGroup.POSITIVE_INFINITY),
                    )
                )
            )
        }

        if (sensorTypeMetadataRepos.findAll(
                Example.of(SensorTypeMetadata(type = SensorType.HUMIDITY))
            ).isEmpty()
        ) {
            sensorTypeMetadataRepos.save(
                SensorTypeMetadata(
                    type = SensorType.HUMIDITY,
                    content = mutableMapOf(
                        SensorValueGroup.LOW.name to RangeDTO(SensorValueGroup.NEGATIVE_INFINITY, 150F),
                        SensorValueGroup.HIGH.name to RangeDTO(151F, SensorValueGroup.POSITIVE_INFINITY),
                    )
                )
            )
        }

    }

    private fun createSensorRecord(sensor: Sensor, greenhouse: Greenhouse, date: LocalDateTime) {
        sensorRecordRepos.save(
            SensorRecord(
                greenhouseId = greenhouse.id,
                sensorId = sensor.id,
                createdAt = date,
                sensorData = RandomStringUtils.randomNumeric(3),
            )
        )
    }

    private fun createIrrigatorRecord(actuator: Actuator, date: LocalDateTime) {
        irrigatorRecordRepos.save(
            NutrientIrrigatorRecord(
                actuatorId = actuator.id,
                runDate = date
            )
        )
}


}