package tik.prometheus.rest.configurations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tik.prometheus.rest.MqttClientFactory

@Configuration
open class MqttConfiguration @Autowired constructor(
    private val configurations: Configurations,
) {
    @Bean
    open fun clientFactory(): MqttClientFactory{
        val url = "tcp://%s:%s".format(configurations.brokerHost, configurations.brokerPort)
        return MqttClientFactory(url)
    }

}