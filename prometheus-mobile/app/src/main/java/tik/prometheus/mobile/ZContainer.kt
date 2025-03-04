package tik.prometheus.mobile

import tik.prometheus.mobile.repository.*

class ZContainer {

    val restServiceApi = RestServiceHelper.createApi()

    val greenhouseRepository = GreenhouseRepository(restServiceApi)

    val sensorRepository = SensorRepository(restServiceApi)

    val actuatorRepository = ActuatorRepository(restServiceApi)
}