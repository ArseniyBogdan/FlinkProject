package ru.flproject.sink.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.springframework.stereotype.Component;
import ru.flproject.schema.Publication;

import static ru.flproject.config.ObjectMapperConfig.createObjectMapper;

@Component
@RequiredArgsConstructor
public class PublicationSerializationSchema implements SerializationSchema<Publication> {
    private static final long serialVersionUID = 1L;

    private transient ObjectMapper objectMapper;

    @Override
    public void open(InitializationContext context) {
        objectMapper = createObjectMapper();
    }

    @Override
    @SneakyThrows
    public byte[] serialize(Publication publication) {
        return objectMapper.writeValueAsBytes(publication);
    }
}
