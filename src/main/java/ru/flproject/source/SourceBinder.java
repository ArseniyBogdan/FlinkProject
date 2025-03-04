package ru.flproject.source;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface SourceBinder <T> {
    DataStream<T> bindSource(StreamExecutionEnvironment env);
}