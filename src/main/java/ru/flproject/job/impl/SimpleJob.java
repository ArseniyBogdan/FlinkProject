package ru.flproject.job.impl;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSink;
import org.springframework.stereotype.Component;
import ru.flproject.job.FlinkJob;

@Component
public class SimpleJob extends FlinkJob {

    @Override
    public void registerJob(StreamExecutionEnvironment env) {
        GeneratorFunction<Long, String> generatorFunction = index -> index + " Customer";
        long numberOfRecords = Long.MAX_VALUE;

        DataGeneratorSource<String> source = new DataGeneratorSource<>(
                generatorFunction,
                numberOfRecords,
                RateLimiterStrategy.perSecond(100),
                Types.STRING
        );

        DataStreamSource<String> dataStream = env.fromSource(
                source,
                WatermarkStrategy.noWatermarks(),
                "Generator Source"
        );

        dataStream
                .filter(s -> Long.parseLong(s.substring(0, s.indexOf(" "))) % 2 == 0)
                .map(evenCustomer -> evenCustomer + ". Hello even customer!")
                .sinkTo(new PrintSink<>());
    }
}
