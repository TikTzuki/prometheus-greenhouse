package tik.prometheus.rest.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tik.prometheus.rest.models.Farm

interface FarmRepos : JpaRepository<Farm, Long> {
}