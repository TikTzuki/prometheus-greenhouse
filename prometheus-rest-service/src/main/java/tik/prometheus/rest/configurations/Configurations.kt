package tik.prometheus.rest.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class Configurations(
    @Value("\${broker.host}")
    var brokerHost: String? = null,
    @Value("\${broker.port}")
    var brokerPort: String? = null,
    var debug: Boolean? = null,
    var mqttClientId: String = UUID.randomUUID().toString()
)