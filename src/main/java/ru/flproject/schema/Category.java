package ru.flproject.schema;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Value;

import java.io.IOException;
import java.util.Locale;

import static java.util.Objects.requireNonNullElse;

public interface Category {
    String value();

    static Category of(String value){
        final String stringValue = requireNonNullElse(value, "");
        try{
            return Category.Enum.parse(stringValue);
        } catch (IllegalArgumentException exc){
            return new Simple(value);
        }
    }

    enum Enum implements Category {
        ANIMALS, WORK, VACATION, MUSIC;

        public static Enum parse(String rawValue){
            if (rawValue == null){
                throw new IllegalArgumentException("Raw value cannot be null");
            }
            for (Enum enumValue: values()){
                if(enumValue.name().equals(rawValue.toUpperCase(Locale.ROOT).trim())){
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("Cannot parse enum from raw value: " + rawValue);
        }


        @Override
        public String value() {
            return name();
        }
    }

    @Value
    class Simple implements Category{
        String value;

        @Override
        @JsonValue
        public String value() {
            return value;
        }
    }

    class Deserializer extends StdDeserializer<Category> {
        private static final long serialVersionUID = 1L;

        protected Deserializer() {
            super(Category.class);
        }

        @Override
        public Category deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Category.of(p.getValueAsString());
        }
    }

    class Serializer extends StdSerializer<Category> {
        private static final long serialVersionUID = 1L;

        protected Serializer() {
            super(Category.class);
        }

        @Override
        public void serialize(Category value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.value());
        }
    }
}

