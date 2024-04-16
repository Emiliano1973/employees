package com.employees.demo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Objects;

public class GenderJsonSerializer extends StdSerializer<Gender> {

    public GenderJsonSerializer() {
        super(Gender.class);
    }

    @Override
    public void serialize(final Gender gender, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(gender)) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeString(gender.getGender());
    }
}
