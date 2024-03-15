package com.employees.demo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GenderJsonSerializer extends StdSerializer<Gender> {
    protected GenderJsonSerializer() {
        super(Gender.class);
    }

    @Override
    public void serialize(Gender gender, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(gender==null){
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeString(gender.getSex());
    }
}
