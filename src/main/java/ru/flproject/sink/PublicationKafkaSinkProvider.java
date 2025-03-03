package ru.flproject.sink;

import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.springframework.stereotype.Component;
import ru.flproject.properties.KafkaProperties;
import ru.flproject.schema.Publication;

@Component
@RequiredArgsConstructor
public class PublicationKafkaSinkProvider implements SinkProvider<Publication> {
    private final KafkaProperties kafkaProperties;
    private final SerializationSchema<Publication> serializationPublicationSchema;

    @Override
    public Sink<Publication> createSink() {
        return KafkaSink.<Publication>builder()
                .setBootstrapServers(kafkaProperties.getBootstrapServers())
                .setRecordSerializer(KafkaRecordSerializationSchema.<Publication>builder()
                        .setTopic(kafkaProperties.getTopics().getPublicationTopic())
                        .setValueSerializationSchema(serializationPublicationSchema)
                        .build())
                .setDeliveryGuarantee(DeliveryGuarantee.NONE)
                .build();
    }
}
