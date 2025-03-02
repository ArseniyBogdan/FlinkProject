package ru.flproject.job;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public abstract class FlinkJob {
    public abstract void registerJob(StreamExecutionEnvironment env);
}
