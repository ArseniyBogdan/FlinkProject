package ru.flproject.job.impl;

import lombok.AllArgsConstructor;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.flproject.job.FlinkJob;
import ru.flproject.schema.Category;
import ru.flproject.schema.Publication;
import ru.flproject.sink.PublicationKafkaSinkProvider;
import ru.flproject.sink.SinkProvider;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Component
@AllArgsConstructor
@ConditionalOnProperty("jobs.kafka-data-gen-job.enabled")
public class KafkaDataGenJob extends FlinkJob {
    private final PublicationKafkaSinkProvider sinkProvider;

    @Override
    public void registerJob(StreamExecutionEnvironment env) {
        GeneratorFunction<Long, Publication> generatorFunction = index -> {
            String[] categoriesForGeneration = {"animals", "work", "vacation", "music", "ocean"};
            String[] nicknamesForGeneration = {"Logger", "Healthy", "Karel", "Vitaly", "Indi"};
            Random generator = new Random();
            UUID publicationUUID = UUID.randomUUID();
            int randomCategoryIndex = generator.nextInt(categoriesForGeneration.length);
            Category category = Category.of(categoriesForGeneration[randomCategoryIndex]);
            String text = "SomeText";
            int randomNicknameIndex = generator.nextInt(nicknamesForGeneration.length);
            String authorNickname = nicknamesForGeneration[randomNicknameIndex];
            return Publication.builder()
                    .publicationUUID(publicationUUID)
                    .category(category)
                    .text(text)
                    .authorNickname(authorNickname)
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                    .build();
        };

        long numberOfRecords = Long.MAX_VALUE;

        DataGeneratorSource<Publication> source = new DataGeneratorSource<>(
                generatorFunction,
                numberOfRecords,
                RateLimiterStrategy.perSecond(1),
                Types.GENERIC(Publication.class)
        );

        DataStreamSource<Publication> dataStream = env.fromSource(
                source,
                WatermarkStrategy.noWatermarks(),
                "Publication generator Source"
        );

        final var sink = sinkProvider.createSink();

        dataStream.sinkTo(sink);
    }
}
