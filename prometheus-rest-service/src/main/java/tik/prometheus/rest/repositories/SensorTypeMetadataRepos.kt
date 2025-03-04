package tik.prometheus.rest.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.SensorTypeMetadata

interface SensorTypeMetadataRepos : JpaRepository<SensorTypeMetadata, SensorType>