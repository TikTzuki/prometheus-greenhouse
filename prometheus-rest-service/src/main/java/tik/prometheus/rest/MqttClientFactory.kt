package tik.prometheus.rest

import org.eclipse.paho.client.mqttv3.MqttClient
import java.util.*

class MqttClientFactory(
    val url: String
) {
    fun create(): MqttClient {
        return MqttClient(url, UUID.randomUUID().toString())
    }
}