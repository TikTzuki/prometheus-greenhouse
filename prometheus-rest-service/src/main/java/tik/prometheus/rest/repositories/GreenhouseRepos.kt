package tik.prometheus.rest.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.models.Greenhouse
import java.util.*

interface GreenhouseRepos : JpaRepository<Greenhouse, Long> {
    @Query(
        """SELECT g 
        FROM Greenhouse g 
        WHERE (g.farmId = :farmId or :farmId is null)
        AND (g.type = :type or :type is null)
        AND (lower(g.label) LIKE lower(concat('%', :label,'%')) or :label is null)
        """
    )
    fun findAllWithParams(@Param("farmId") farmId: Long?, @Param("label") label: String?, @Param("type") type: GreenhouseType?, pageable: Pageable): Page<Greenhouse>

}