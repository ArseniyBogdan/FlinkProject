package ru.flproject.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("kafka")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class KafkaProperties {
    @NotNull(message = "Kafka group-id cannot be null")
    private final String groupId;
    @NotNull(message = "Kafka bootstrap servers cannot be null")
    private final String bootstrapServers;
    @NotNull(message = "Topics cannot be null")
    private final Topics topics;

    @ConfigurationProperties(prefix = "topics")
    @ConstructorBinding
    @RequiredArgsConstructor
    @Getter
    public static class Topics {
        @NotEmpty(message = "Publication Topic cannot be null or empty")
        private final String publicationTopic;
    }
}
