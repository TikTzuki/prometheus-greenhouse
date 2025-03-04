package tik.prometheus.rest.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.dtos.*
import tik.prometheus.rest.models.ActuatorAllocation
import tik.prometheus.rest.models.Greenhouse
import tik.prometheus.rest.models.SensorAllocation
import tik.prometheus.rest.repositories.GreenhouseRepos
import tik.prometheus.rest.services.GreenhouseService

@Service
class GreenhouseServiceImpl @Autowired constructor(
    private val greenhouseRepos: GreenhouseRepos,
) : GreenhouseService {
    override fun getGreenhouses(farmId: Long?, label: String?, greenhouseType: GreenhouseType?, pageable: Pageable): Page<GreenhouseLiteDTO> {
        val pageEntity = greenhouseRepos.findAllWithParams(farmId, label, greenhouseType, pageable)
        return pageEntity.map(Greenhouse::toGreenhouseSummaryDTO)
    }

    override fun getGreenhouse(id: Long): GreenhouseDTO {
        return greenhouseRepos.findById(id).map {
            GreenhouseDTO(
                it.id,
                it.farmId,
                it.type,
                it.area,
                it.height,
                it.width,
                it.length,
                it.cultivationArea,
                it.actuatorAllocations.map(ActuatorAllocation::toActuatorLiteDTO),
                it.sensorAllocations.map(SensorAllocation::toSensorLiteDTO),
                it.label
            )
        }.orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "No result found") }
    }

    override fun updateGreenhouse(id: Long, greenhouseDTO: GreenhouseDTO): GreenhouseDTO {
        val gh = greenhouseRepos.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No result found")
        gh.type = greenhouseDTO.type
        gh.cultivationArea = greenhouseDTO.cultivationArea
        gh.area = greenhouseDTO.area
        gh.height = greenhouseDTO.height
        gh.width = greenhouseDTO.width
        gh.length = greenhouseDTO.length
        gh.label = greenhouseDTO.label
        greenhouseRepos.saveAndFlush(gh)
        return gh.toGreenhouseDTO()
    }
}