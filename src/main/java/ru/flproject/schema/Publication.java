package ru.flproject.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Publication {
    @JsonPropertyDescription("Publication uuid")
    private UUID publicationUUID;

    @JsonPropertyDescription("Publication category")
    @JsonDeserialize(using = Category.Deserializer.class)
    @JsonSerialize(using = Category.Serializer.class)
    private Category category;

    @JsonPropertyDescription("Publication Text")
    private String text;

    @JsonPropertyDescription("Publication author's nickname")
    private String authorNickname;

    @JsonPropertyDescription("Timestamp")
    private Long timestamp;
}
