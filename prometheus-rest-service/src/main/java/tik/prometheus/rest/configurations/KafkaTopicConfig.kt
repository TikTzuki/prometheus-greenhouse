package tik.prometheus.rest.configurations

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin


//@Configuration
//open class KafkaTopicConfig {
//    @Value(value = "\${kafka.bootstrapAddress}")
//    private val bootstrapAddress: String? = null
//
//    @Bean
//    open fun kafkaAdmin(): KafkaAdmin {
//        val configs: MutableMap<String, Any?> = HashMap()
//        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
//        return KafkaAdmin(configs)
//    }
//
//    @Bean
//    open fun sensorTopic(): NewTopic {
//        return NewTopic("ora-SENSOR_RECORD-jdbc-00", 1, 1.toShort())
//    }
//}