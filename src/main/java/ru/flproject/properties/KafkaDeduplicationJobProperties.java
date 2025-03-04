package ru.flproject.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.time.Time;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@ConfigurationProperties("jobs.kafka-deduplication-job")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class KafkaDeduplicationJobProperties {
    private final boolean enabled;
    @NotNull
    private final OperatorProperties operators;

    @ConfigurationProperties("operators")
    @ConstructorBinding
    @RequiredArgsConstructor
    @Getter
    public static class OperatorProperties {
        @NotNull
        private final DeduplicatorProperties deduplicator;

        @ConfigurationProperties("deduplicator")
        @ConstructorBinding
        @RequiredArgsConstructor
        @Getter
        public static class DeduplicatorProperties {
            @NotNull
            private final Map<DeduplicatorName, Duration> ttl;

            public Time getTtl(DeduplicatorName name) {
                return Optional.ofNullable(ttl.get(name))
                        .map(duration -> Time.milliseconds(duration.toMillis()))
                        .orElseThrow(() -> new IllegalArgumentException("Not found TTL by Deduplicator name: " + name));
            }

            public enum DeduplicatorName {
                WORK
            }
        }
    }

}
