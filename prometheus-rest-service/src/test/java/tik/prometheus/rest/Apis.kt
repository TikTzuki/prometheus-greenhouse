package tik.prometheus.rest

class Apis {
    companion object {
        const val sensorGet = "/sensors/%s"
        const val sensorPut = "/sensors/{}"
        const val sensorDelete = "/sensors/{}"
        const val sensorGetList = "/sensors"
        const val sensorRecordGetList = "/sensors/{}/records"

        const val greenhouseGet = "/greenhouses/{}"
        const val greenhousePut = "/greenhouses/{}"
        const val greenhouseDelete = "/greenhouses/{}"
        const val greenhouseGetList = "/greenhouses"
        const val greenhousePost = "/greenhouses"

        const val actuatorGet = "/actuator/{}"
        const val actuatorPut = "/actuator/{}"
        const val actuatorDelete = "/actuator/{}"
        const val actuatorPatch = "/actuator/{}"
        const val actuatorTaskPost = "/actuator/{}/tasks"
        const val actuatorGetList = "/actuator"
        const val actuatorTaskGetList = "/actuator/{}/tasks"
    }
}