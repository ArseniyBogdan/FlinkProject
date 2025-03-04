package ru.flproject.source;

import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.springframework.stereotype.Component;
import ru.flproject.properties.KafkaProperties;
import ru.flproject.schema.Publication;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PublicationKafkaSourceBinder implements SourceBinder<Publication> {
    private final KafkaProperties kafkaProperties;
    private final DeserializationSchema<Publication> deserializationSchema;

    @Override
    public DataStream<Publication> bindSource(StreamExecutionEnvironment env) {
        final String sourceName = "Publication topic kafka";
        return env.fromSource(
                KafkaSource.<Publication>builder()
                        .setBootstrapServers(kafkaProperties.getBootstrapServers())
                        .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                        .setGroupId(kafkaProperties.getGroupId())
                        .setTopics(kafkaProperties.getTopics().getPublicationTopic())
                        .setValueOnlyDeserializer(deserializationSchema)
                        .build(),
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(1L)),
                sourceName
        ).name(sourceName).uid(sourceName + "_id");
    }
}
