package ru.flproject;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSink;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hellow world!");
        try {
            final StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
            streamEnv.setParallelism(2);

            GeneratorFunction<Long, String> generatorFunction = index -> index + " Customer";
            long numberOfRecords = Long.MAX_VALUE;

            DataGeneratorSource<String> source = new DataGeneratorSource<>(
                    generatorFunction,
                    numberOfRecords,
                    RateLimiterStrategy.perSecond(100),
                    Types.STRING
            );

            DataStreamSource<String> dataStream = streamEnv.fromSource(
                    source,
                    WatermarkStrategy.noWatermarks(),
                    "Generator Source"
            );

            dataStream
                    .filter(s -> Long.parseLong(s.substring(0, s.indexOf(" "))) % 2 == 0)
                    .map(evenCustomer -> evenCustomer + ". Hello even customer!")
                    .sinkTo(new PrintSink<>());

            streamEnv.execute("Generating And filtering information example");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}