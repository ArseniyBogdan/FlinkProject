package ru.flproject.source.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.springframework.stereotype.Component;
import ru.flproject.schema.Publication;

import java.io.IOException;

import static ru.flproject.config.ObjectMapperConfig.createObjectMapper;

@Component
@RequiredArgsConstructor
public class PublicationDeserializationSchema implements DeserializationSchema<Publication> {
    private static long serialVersionUID = 1L;

    private transient ObjectMapper objectMapper;

    @Override
    public void open(InitializationContext context) {
        objectMapper = createObjectMapper();
    }

    @Override
    public Publication deserialize(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, Publication.class);
    }

    @Override
    public boolean isEndOfStream(Publication publication) {
        return false;
    }

    @Override
    public TypeInformation<Publication> getProducedType() {
        return TypeInformation.of(Publication.class);
    }
}
