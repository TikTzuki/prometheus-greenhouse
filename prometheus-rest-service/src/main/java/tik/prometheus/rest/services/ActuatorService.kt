package tik.prometheus.rest.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import tik.prometheus.rest.dtos.ActuatorDTO
import tik.prometheus.rest.dtos.ActuatorLiteDTO
import tik.prometheus.rest.dtos.ActuatorTaskDTO
import tik.prometheus.rest.models.Actuator

interface ActuatorService {
    fun getActuators(greenhouseId: Long, pageable: Pageable): Page<ActuatorLiteDTO>
    fun getActuator(id: Long): ActuatorDTO
    fun updateActuator(id: Long, actuator: ActuatorDTO): ActuatorDTO
    fun patchActuator(id: Long, nextState: ActuatorDTO.ActuatorState)

    fun createTask(id: Long, task: ActuatorTaskDTO): ActuatorTaskDTO
    fun getTask(id: Long): ActuatorTaskDTO

    fun deleteTask(id: Long)

    companion object {
        fun actuatorTopic(actuator: Actuator): String {
            return "actuator/%s".format(actuator.id)
        }

    }
}