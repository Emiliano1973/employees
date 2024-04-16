package com.employees.demo.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Objects;

public class GenderJsonDeserializer extends StdDeserializer<Gender> {
    public GenderJsonDeserializer() {
        super(Gender.class);
    }

    @Override
    public Gender deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        String v = jsonParser.getValueAsString();
        if (Objects.isNull(v)) {
            return null;
        }
        return Gender.getGenderByString(v);
    }
}
