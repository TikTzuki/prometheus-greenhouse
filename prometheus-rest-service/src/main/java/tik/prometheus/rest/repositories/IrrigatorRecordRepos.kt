package tik.prometheus.rest.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tik.prometheus.rest.models.NutrientIrrigatorRecord

interface IrrigatorRecordRepos : JpaRepository<NutrientIrrigatorRecord, Long> {
}