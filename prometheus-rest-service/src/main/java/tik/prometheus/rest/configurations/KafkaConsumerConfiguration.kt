package tik.prometheus.rest.configurations//package tik.prometheus.rest
//
//import com.google.gson.JsonObject
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.annotation.EnableKafka
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
//import org.springframework.kafka.core.ConsumerFactory
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory
//import org.springframework.kafka.support.serializer.JsonDeserializer
//
//
//@Configuration
//@EnableKafka
//open class KafkaConsumerConfiguration(
//) {
//
//    @Value(value = "\${kafka.bootstrapAddress}")
//    var bootstrapAddress: String? = null
//
//    @Value(value = "\${spring.kafka.consumer.group-id}")
//    var groupId: String? = null
//
//    @Bean
//    open fun consumerFactory(): ConsumerFactory<String?, JsonObject> {
//        val props: MutableMap<String, Any?> = HashMap()
//        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
//        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
////        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
////        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
//
//        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(JsonObject::class.java))
//    }
//
//    @Bean
//    open fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, JsonObject> {
//        val factory = ConcurrentKafkaListenerContainerFactory<String, JsonObject>()
//        factory.setConsumerFactory(consumerFactory())
//        return factory
//    }
//}